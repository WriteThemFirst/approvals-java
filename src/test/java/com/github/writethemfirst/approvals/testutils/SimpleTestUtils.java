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

import java.nio.file.Path;

import static com.github.writethemfirst.approvals.files.ApprovedAndReceivedPaths.approvedAndReceived;
import static com.github.writethemfirst.approvals.utils.FileUtils.*;
import static java.nio.file.Paths.get;

public class SimpleTestUtils {
    public final Path received;
    public final Path approved;

    public SimpleTestUtils(final String methodName, final Class<?> testClass, String extensionWithDot) {
        final String className = testClass.getSimpleName();
        final Path packageResourcesPath = get("src/test/resources/", testClass.getPackage().getName().split("\\."));
        final Path folderForClass = packageResourcesPath.resolve(className);
        final ApprovedAndReceivedPaths approvedAndReceivedPaths = approvedAndReceived(folderForClass, methodName, extensionWithDot);
        received = approvedAndReceivedPaths.received;
        approved = approvedAndReceivedPaths.approved;
    }

    public void writeReceived(final String content) {
        write(content, received);
    }

    public String readReceived() {
        return silentRead(received);
    }

    public void writeApproved(final String content) {
        write(content, approved);
    }

    public void cleanupPaths() {
        silentRemove(received);
        silentRemove(approved);
    }
}
