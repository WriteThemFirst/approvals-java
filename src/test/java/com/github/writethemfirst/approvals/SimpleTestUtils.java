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
package com.github.writethemfirst.approvals;

import com.github.writethemfirst.approvals.files.ApprovedAndReceivedPaths;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.writethemfirst.approvals.utils.FileUtils.*;

class SimpleTestUtils {
    final Path folderForClass;
    final Path received;
    final Path approved;

    SimpleTestUtils(final String methodName, final Class<?> testClass) {
        final String className = testClass.getSimpleName();
        folderForClass = Paths.get("src\\test\\resources\\com\\github\\writethemfirst\\approvals\\" + className);
        final ApprovedAndReceivedPaths approvedAndReceivedPaths = new ApprovedAndReceivedPaths(
            path(methodName, "approved"),
            path(methodName, "received"));
        received = approvedAndReceivedPaths.received;
        approved = approvedAndReceivedPaths.approved;
    }

    void writeReceived(final String content) {
        write(content, received);
    }

    String readReceived() {
        return silentRead(received);
    }

    void writeApproved(final String content) {
        write(content, approved);
    }

    void cleanupPaths() {
        silentRemove(received);
        silentRemove(approved);
    }


    private Path path(final String methodName, final String extension) {
        return Paths.get(folderForClass.resolve(methodName) + "." + extension);
    }

}
