import Repository from "./Repository";

// Chck proxy configuration
const baseDomain = "";
const baseURL = `${baseDomain}`;
const resource = baseURL + "/api/graph";
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

  refreshMovieActors(id) {
    return Repository.get(`${resource}/relationship/movies/${id}`);
  }, 

  refreshActorsMovies(id) {
    return Repository.get(`${resource}/relationship/actors/${id}`);
  }, 

  getServiceInfo(){
    return resource;
  }

  
};
