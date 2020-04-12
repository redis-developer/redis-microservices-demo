import Repository from "./Repository";

// You can use your own logic to set your local or production domain
const baseDomain = "http://localhost:8081";
// The base URL is empty this time due we are using the jsonplaceholder API
const baseURL = `${baseDomain}`;

const resource = baseURL + "/api/1.0/data-mysql-api";
export default {

  async status() {
    let retValue = {};
    try {
      //retValue = 
      retValue = await Repository.get(`${resource}/status`);
    }
    catch(e) {
      console.log(`Service ${baseDomain} not available `);
      retValue.status = "ERROR";
    }
    return retValue;
  }, 

  start() {
    return Repository.get(`${resource}/start`);
  }, 

  stop() {
    return Repository.get(`${resource}/stop`);
  }, 

  findActors() {
    return Repository.get(`${resource}/actors`);
  },

  findActorById(id) {
    return Repository.get(`${resource}/actors/${id}`);
  },
  
  updateActor(payload) {
    return Repository.post(`${resource}/actors/`, payload);
  },

  findMovies() {
    return Repository.get(`${resource}/movies`);
  },

  findMovieById(id) {
    return Repository.get(`${resource}/movies/${id}`);
  },
  
  findMovieActors(id) {
    return Repository.get(`${resource}/movies/${id}/actors`);
  },

  updateMovie(payload) {
    return Repository.post(`${resource}/movies/`, payload);
  },

  getServiceInfo(){
    return resource;
  }

};
