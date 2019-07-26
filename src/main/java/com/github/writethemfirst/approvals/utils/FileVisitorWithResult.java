package com.github.writethemfirst.approvals.utils;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class FileVisitorWithResult extends SimpleFileVisitor<Path> {
  Stream<Path> result = Stream.empty();
  private BiPredicate<Path, BasicFileAttributes> matcher;

  public FileVisitorWithResult(BiPredicate<Path, BasicFileAttributes> pMatcher) {
    matcher = pMatcher;
  }

  @Override
  public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
    if (matcher.test(file, attrs)) {
      result = Stream.concat(result, Stream.of(file));
    }
    return super.visitFile(file, attrs);
  }

  @Override
  public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
    // A FileSystemLoopException can occur when there is a recursive symbolic link
    // in the directory. This is the case on some Linux distributions like Ubuntu.
    // In that case, we will simply omit the path and continue to walk through the
    // tree.
    // See related issue https://github.com/WriteThemFirst/approvals-java/issues/74
    if (exc instanceof FileSystemLoopException) {
      return FileVisitResult.CONTINUE;
    }
    return super.visitFileFailed(file, exc);
  }
}