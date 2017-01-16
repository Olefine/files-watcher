package ru.egorodov.server.services

import org.scalatest.FunSpec
import com.amazonaws.services.ec2.model.InstanceType
import org.scalamock.scalatest.MockFactory
import ru.egorodov.server.files.FileInfo

class FileInfoMock extends FileInfo("")

class AmazonInstanceSpec extends FunSpec with MockFactory {
  describe("An #instanceType") {
    it("returns R3Large if size between 0 and 12 gb") {
      val fileInfoMock = mock[FileInfoMock]

      (fileInfoMock.size _).expects().returning(2000000000)
      val amazonInstance = new AmazonInstance(fileInfoMock)
      assert(amazonInstance.instanceType == InstanceType.R3Large)
    }
  }
}
