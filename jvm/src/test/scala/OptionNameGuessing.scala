package org.rogach.scallop

class OptionNameGuessing extends ScallopTestBase {

  test ("simple") {
    object Conf extends ScallopConf(Seq("-a", "1")) {
      val appleso = opt[Int]()
      val bananaso = opt[Int]()

      verify()
    }
    Conf.appleso() shouldBe 1
    Conf.bananaso.toOption shouldBe None
  }

  test ("shadowing with derived option") {
    object Conf extends ScallopConf(Seq("--apples", "1")) {
      val apples = opt[Int]()
      val applesPlus = apples.map(_+2)

      verify()
    }
    Conf.apples() shouldBe 1
    Conf.applesPlus() shouldBe 3
    Conf.apples.isSupplied shouldBe true
    Conf.applesPlus.isSupplied shouldBe true
  }

  test ("tricky") {
    object Conf extends ScallopConf(Seq("-a", "1")) {
      val appleso = opt[Int]()
      val applesPlus = appleso.map(_+2)
      lazy val applesVal = appleso()
      val bananaso = opt[Int]()
      val aaa = opt[Int]()

      verify()
    }
    Conf.appleso() shouldBe 1
    Conf.applesPlus() shouldBe 3
    Conf.bananaso.toOption shouldBe None
    Conf.aaa.toOption shouldBe None
  }

  test ("camelCase convert") {
    object Conf extends ScallopConf(Seq("--apple-treeo", "1")) {
      val appleTreeo = opt[Int]()

      verify()
    }
    Conf.appleTreeo() shouldBe 1
  }

  test ("guessing in subcommands") {
    object Conf extends ScallopConf(Seq("tree", "--apples", "3")) {
      object tree extends Subcommand("tree") {
        val apples = opt[Int]()
      }
      addSubcommand(tree)

      verify()
    }
    Conf.tree.apples() shouldBe 3
  }

  test ("replacing of the name in mapped ScallopOptions") {
    object Conf extends ScallopConf(Nil) {
      val xs = opt[Int](default = Some(0)).map(_+1)

      verify()
    }
    Conf.xs.toOption shouldBe Some(1)
  }

  test ("replacing of the name in doubly mapped ScallopOptions") {
    object Conf extends ScallopConf(Nil) {
      val xs = opt[Int](default = Some(0)).map(_+1).map(_.toString)

      verify()
    }
    Conf.xs.toOption shouldBe Some("1")
  }

  test ("trailArg name guessing") {
    object Conf extends ScallopConf(List("1", "foo", "bar", "baz", "bippy")) {
      val trailing1 = trailArg[Int]()
      val trailing2 = trailArg[List[String]]()

      verify()
    }
    Conf.trailing1.name shouldBe "trailing1"
    Conf.trailing2.name shouldBe "trailing2"
    Conf.trailing1.toOption shouldBe Some(1)
    Conf.trailing2.toOption shouldBe Some(List("foo", "bar", "baz", "bippy"))
  }

  test ("toggle name guessing") {
    object Conf extends ScallopConf(List("--foo", "--nobippy", "-s")) {
      val foo = toggle()
      val zoop = toggle()
      val bippy = toggle()
      val scooby = toggle()

      verify()
    }
    Conf.foo.name shouldBe "foo"
    Conf.zoop.name shouldBe "zoop"
    Conf.bippy.name shouldBe "bippy"
    Conf.foo.toOption shouldBe Some(true)
    Conf.zoop.toOption shouldBe None
    Conf.bippy.toOption shouldBe Some(false)
    Conf.scooby.toOption shouldBe Some(true)
  }

  test ("decode special symbols") {
    object Conf extends ScallopConf(List()) {
      val `source-folder` = opt[String]()
      verify()
    }
    Conf.`source-folder`.name shouldBe "source-folder"
  }

  test ("option groups") {
    object Conf extends ScallopConf(List("--foo", "--bar")) {
      val g = group()
      val foo = opt[Boolean](group = g)
      val bar = opt[Boolean]()
      verify()
    }
    Conf.foo() shouldBe true
    Conf.bar() shouldBe true
  }

}
