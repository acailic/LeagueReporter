# League Reporter
Android app for University
 

EUROPEAN LEAGUES FOOTBALL INFORMATION, BOOKIE ASSISTANT

THEME : BLUE
 
 football-data.org is a RESTful API in beta with regularly updated data. If you register for a free API key you will get CORS support. I recommend registering for a key to show your support and help the service track usage. However, a key is not required yet so you can try out the endpoints right now. I am excited to see this API grow and mature!
 
 Available endpoints
 
 /soccerseasons/
 /soccerseasons/{id}/ranking
 /soccerseasons/{id}/fixtures
 /fixtures
 /soccerseasons/{id}/teams
 /teams/{id}
 /teams/{id}/fixtures/
 Some example calls:
 
 http://api.football-data.org/v1/soccerseasons
 http://api.football-data.org/v1/soccerseasons/351/teams
 http://api.football-data.org/v1/fixtures
 http://api.football-data.org/v1/teams/5
 http://api.football-data.org/v1/soccerseasons/424 (European Championships France 2016)
 Example JSON output for a team:
 
 {
    "_links":{
       "self":{
          "href":"http://api.football-data.org/v1/teams/5"
       },
       "fixtures":{
          "href":"http://api.football-data.org/v1/teams/5/fixtures"
       },
       "players":{
          "href":"http://api.football-data.org/v1/teams/5/players"
       }
    },
    "name":"FC Bayern München",
    "code":"FCB",
    "shortName":"Bayern",
    "squadMarketValue":"559,100,000 €",
    "crestUrl":"http://upload.wikimedia.org/wikipedia/commons/c/c5/Logo_FC_Bayern_München.svg"
 }
 
 
 Links: 
 
 http://www.jokecamp.com/blog/guide-to-football-and-soccer-data-and-apis/#footballdata