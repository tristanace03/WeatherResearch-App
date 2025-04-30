import java.util.Scanner;

public class WeatherController{
  // private LogbookManager logbookManager;
  private DatabaseManager databaseManager;
  private UserManager userManager;
  private ESPNScores espnScores;

  public WeatherController(DatabaseManager dm, UserManager um, ESPNScores espn){
    this.databaseManager = dm;
    this.userManager = um;
    this.espnScores = espn;

  }

  // public addObservation{
  // }

  // public getObservation{
  // }

  // public saveObservation{
  // }

  // public queryObservations{
  // }

  // public saveUserPreferences{
  // }

  // public getUserPreferences{
  // }

  // public getWeatherData{
  // }

  public void getESPNScores(){

    Scanner scan = new Scanner(System.in);
    int choice = 0;

    while(choice < 1 || choice > 2){
      System.out.print("Please choose 1 for baseball or 2 for NBA: ");
      if(scan.hasNextInt()){
        choice = scan.nextInt();
        if(choice >=1 && choice <= 2){
          break;
        }
        else{
          System.out.println("Invalid Input");
        }
      }
      else{
        String invalid = scan.next();
        System.out.println(invalid + " is not a valid input");
      }
    }
    
    if(choice == 1){
      System.out.println("Here are the current MLB Scores for Today:");
      espnScores.getScores("baseball", "mlb");
    }
    else if(choice == 2){
      System.out.println("Here are the current NBA Scores for Today:");
      espnScores.getScores("basketball", "nba");
    }
    



  }

}
