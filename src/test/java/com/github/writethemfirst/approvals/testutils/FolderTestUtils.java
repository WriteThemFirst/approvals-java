/*
 * Approvals-Java - Approval testing library for Java. Alleviates the burden of hand-writing assertions.
 * Copyright © 2018 Write Them First!
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.writethemfirst.approvals.testutils;

import com.github.writethemfirst.approvals.files.ApprovedAndReceivedPaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.github.writethemfirst.approvals.files.ApprovedAndReceivedPaths.approvedAndReceived;
import static com.github.writethemfirst.approvals.utils.FileUtils.silentRecursiveRemove;
import static com.github.writethemfirst.approvals.utils.FileUtils.write;
import static java.nio.file.Paths.get;

public class FolderTestUtils {
    public final Path received;
    public final Path approved;
    public final Path actual;

    public FolderTestUtils(final String methodName, final Class<?> testClass) throws IOException {
        final String className = testClass.getSimpleName();
        final Path packageResourcesPath = get("src/test/resources/", testClass.getPackage().getName().split("\\."));
        final Path folderForClass = packageResourcesPath.resolve(className);
        final ApprovedAndReceivedPaths approvedAndReceivedPaths = approvedAndReceived(folderForClass, methodName);
        received = approvedAndReceivedPaths.received;
        approved = approvedAndReceivedPaths.approved;
        actual = Files.createTempDirectory(methodName);
    }

    public void writeActual(final String content, final String fileName) {
        write(content, actual.resolve(fileName));
    }

    public void writeApproved(final String content, final String fileName) {
        write(content, approved.resolve(fileName));
    }

    public void writeReceived(final String content, final String fileName) {
        write(content, received.resolve(fileName));
    }

    public void cleanupPaths() {
        silentRecursiveRemove(received);
        silentRecursiveRemove(approved);
    }
}