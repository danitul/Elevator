/**
 *
 * @param id            elevator unique id
 * @param total_floors  total elevator floors (it's the same as the top floor as it does not include the ground floor)(for elevators that go underground will only require a change in the view presented to the passengers)
 * @param current_floor the floor where the elevator finds itself currently
 * @param goal_floors   a list of floors where passengers will get off
 * @param pickup_orders a map of floors where passengers will be picked up along with their desired direction
 * @param direction     the direction of movement of the elevator (-1, 0, 1)
 */
case class Elevator(
                     id: Int,
                     total_floors: Int,
                     current_floor: Int,
                     goal_floors: List[Int],
                     pickup_orders: Map[Int, Int],
                     direction: Int = 0
                   ) {
  /**
   * current status of the elevator
   */
  val status: Elevator = this

  def update(floor: Int, direction: Int, goal_floor: Int): Elevator = {
    this.status
  }

  /**
   * Will add a new pick up request in the pickup map for the current elevator
   * @param pickup_floor
   * @param direction
   * @return
   */
  def pickup(pickup_floor: Int, direction: Int): Elevator = {
    this.copy(pickup_orders = pickup_orders ++ Map(pickup_floor -> direction))
  }

  /**
   * Moves elevator one step further in either direction based on closest drop off and closest pick up floors as well as current direction
   * Prioritizes dropping off passenger in the current direction, while also picking up passengers going in the same direction
   * When there are no more drop offs or pick ups in set direction, elevator changes direction
   * Elevator stops when there are no more drop offs or pick ups left.
   * Detinations for new pickups are randomly generated at the moment.
   * @return
   */
  def step() = {
    if (goal_floors.isEmpty && pickup_orders.isEmpty)
      this.copy(direction = 0)
    else {
      val next_stop: Int = if (direction == 1) {
        Math.min(closest_drop_off(goal_floors), closest_pickup(pickup_orders))
      } else if (direction == -1) {
        Math.max(closest_drop_off(goal_floors), closest_pickup(pickup_orders))
      } else current_floor + direction
      val new_direction: Int = if (next_stop == current_floor) direction * (-1) else direction
      this.copy(
        id = id,
        total_floors = total_floors,
        current_floor = next_stop,
        goal_floors = goal_floors diff List(next_stop), //should also add here desired goal floors of people getting on, for now generate_destination randomly
        pickup_orders = pickup_orders - next_stop,
        direction = new_direction
      )
    }
  }

  /**
   * randomly generates a goal for the newly picked up passanger
   * @return
   */
  private def generate_destination(): Int = {
    val rnd = new scala.util.Random
    if (direction == 1) current_floor + rnd.nextInt((total_floors - current_floor) + 1)
    else rnd.nextInt(total_floors - current_floor)
  }

  /**
   * Computes closest drop off floor on the direction this elevator is going
   * @param drop_off_floors
   * @return
   */
  private def closest_drop_off(drop_off_floors: List[Int]): Int = {
    if (direction == 1 && current_floor < total_floors && !drop_off_floors.isEmpty)
      drop_off_floors.min
    else if (direction == -1 && current_floor > 0 && !drop_off_floors.isEmpty)
      drop_off_floors.max
    else if (direction == 1) current_floor + 1 else current_floor - 1
  }

  /**
   * Computes closest floor for pickup based on the direction this elevator is going
   * @param pickup
   * @return
   */
  private def closest_pickup(pickup: Map[Int, Int]): Int = {
    if (direction == 1 && current_floor < total_floors && !pickup.isEmpty) {
      val closest: Iterator[Int] = pickup.filter({case (_,dir) => dir == direction }).keysIterator.filter(key => key >= current_floor)
      if (closest.isEmpty) current_floor else closest.min
    } else if (direction == -1 && current_floor > 0 && !pickup.isEmpty) {
      val closest: Iterator[Int] = pickup.filter({case (_,dir) => dir == direction }).keysIterator.filter(key => key <= current_floor)
      if (closest.isEmpty) current_floor else closest.max
    }
    else if (direction == 1) current_floor + 1 else current_floor - 1
  }
}
