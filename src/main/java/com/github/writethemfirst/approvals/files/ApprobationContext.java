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
package com.github.writethemfirst.approvals.files;

import java.nio.file.Path;

import static java.lang.String.format;

/**
 * # ApprobationContext
 *
 * An {@link Approbation} requires 2 information to be computed: the folder in which the approval files are located
 * for the current execution context, and the actual name of the approval file to search for.
 *
 * The {@link ApprobationContext} POJO is only here to store those 2 information so they can later be used for
 * managing the approval files.
 *
 * @author aneveux
 * @version 1.0
 * @since 1.2
 */
public class ApprobationContext {

    /**
     * The folder in which the approval files are to be searched for
     */
    public final Path folder;

    /**
     * The fileName to search for while looking after the approval files.
     *
     * Warning: this is only the file name, and doesn't contain the extension, which needs to be added depending on
     * the kind of approval file you want to retrieve.
     */
    public final String fileName;

    /**
     * Constructs an {@link ApprobationContext} which can later be used in order to retrieve particular approval
     * files.
     *
     * @param folder   The folder in which the approval files are to be searched for
     * @param fileName The file name you to search for while looking after the approval files. Warning: shouldn't
     *                 contain the file extension.
     */
    ApprobationContext(final Path folder, final String fileName) {
        this.folder = folder;
        this.fileName = fileName;
    }

    /**
     * Returns the path to a dedicated folder for the current {@link Approbation}.
     *
     * That folder may be used for storing multiple files which will later be compared for approval.
     *
     * The folder name will be the method name followed by `.Files`.
     *
     * @return The path to a folder dedicated to storing the approvals files of the current context.
     */
    public Path approvalsFolder() {
        final String folderName = format("%s.approved", fileName);
        return folder.resolve(folderName);
    }

}
