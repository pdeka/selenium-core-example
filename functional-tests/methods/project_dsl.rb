module ProjectDsl

  def resetSystem
    openLocation "/"
  end

  def openLocation(name)
    @s.open(name)
  end

end
