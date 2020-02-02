/*
 * Copyright (c) 2019. Yuriy Stul
 */

package com.stulsoft.akka.stream.scala.basics

import java.io.File
import java.nio.file.Path

import scala.util.Try

/**
 * @author Yuriy Stul
 */
object Utils {
  def pathFromResource(fileName: String): Try[Path] = {
    Try {
      val path = getClass.getClassLoader.getResource(fileName).getPath
      val file = new File(path)
      file.toPath
    }
  }
}
