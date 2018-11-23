package basic;

import java.util.ArrayList;

public class Squad {

  private ArrayList<Spaceship> spaceships;
  private Planet destination;

  public Squad(ArrayList<Spaceship> spaceships, Planet destination) {
    this.spaceships = spaceships;
    this.destination = destination;
  }

  public void addSpaceship(Spaceship spaceship) {
    spaceships.add(spaceship);
  }

  public void changeDestination(Planet destination) {
    if (!this.destination.equals(destination)) {
      this.destination = destination;
    }
  }
  
  public ArrayList<Spaceship> getSpaceships() {
	    return spaceships;
	  }

}
