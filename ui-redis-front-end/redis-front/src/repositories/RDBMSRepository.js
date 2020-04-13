import Repository from "./Repository";

// Checl proxy configuration
const baseDomain = "";
const baseURL = `${baseDomain}`;
const resource = baseURL + "/api/legacy/";
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
