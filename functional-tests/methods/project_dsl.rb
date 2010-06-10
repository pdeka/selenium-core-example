#Your project level DSL functions. The vocabulary that you use in your project to communicate.
module ProjectDsl

  def resetSystem
    openLocation "/"
  end

  def openLocation(name)
    @s.open(name)
  end

end
