import org.junit._
import org.junit.Assert.assertEquals

class ElevatorSuites {
  @Test def `Elevator returns it's own state`: Unit = {
    val elevator_1 = new Elevator(1, 5, 0, List(3), Map(0 -> 1))
    Assert.assertEquals(Elevator(1,5,0,List(3),Map(0 -> 1),0), elevator_1.status)
  }

  @Test def `Elevator picks up passenger`: Unit = {
    val elevator_1 = new Elevator(1, 5, 0, List(3), Map(0 -> 1))
    Assert.assertEquals(
      Elevator(1,5,0,List(3),Map(0 -> 1, 3 -> 1),0),
      elevator_1.pickup(3, 1)
    )
  }

  @Test def `Elevator drops off passenger at goal floor`: Unit = {
    val elevator_1 = new Elevator(1, 8, 2, List(3,4), Map(0 -> -1, 5 -> 1), 1)

    Assert.assertEquals(Elevator(1,8,3,List(4),Map(0 -> -1, 5 -> 1),1), elevator_1.step )
  }

    @Test def `Elevator prioritizes dropping off passengers first before picking up new ones`: Unit = {
      val elevator_1 = new Elevator(1, 8, 2, List(3,5), Map(0 -> -1, 4 -> 1), 1)
      val elevator_step_1 = Elevator(1,8,3,List(5),Map(0 -> -1, 4 -> 1),1)
      val elevator_step_2 = Elevator(1,8,4,List(5),Map(0 -> -1),1)
      Assert.assertEquals(elevator_step_1, elevator_1.step )
      Assert.assertEquals(elevator_step_2, elevator_step_1.step )
    }

  @Test def `Elevator picking up new passenger if they are on the current direction even if no one else needs to be dropped off`: Unit = {
    val elevator_1 = new Elevator(1, 8, 3, List(), Map(5 -> 1),1)

    Assert.assertEquals(Elevator(1,8,5,List(),Map(),1), elevator_1.step.step)
  }

  @Test def `Elevator changes direction if there's no more action to be done in the current direction`: Unit = {
    val elevator_1 = new Elevator(1, 8, 6, List(), Map(5 -> -1),1)

    Assert.assertEquals(Elevator(1,8,5,List(),Map(),-1), elevator_1.step.step)
  }

/*
   @Test def `Elevator works correctly on the edges(ground floor, last floor)`: Unit = {
    val elevator_1 = new Elevator(1, 8, 6, List(), Map(5 -> -1),1)

    Assert.assertEquals(Elevator(1,8,5,List(),Map(),-1), elevator_1.step.step)
  }

 @Test def `Elevator picking up new passenger should add new goal floor`: Unit = {
    val elevator_1 = new Elevator(1, 8, 3, List(), Map(5 -> 1))

    Assert.assertEquals(Elevator(1,8,3,List(),Map(),0), elevator_1.step)
  }*/
}
