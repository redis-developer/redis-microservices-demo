import Repository from "./Repository";

// look in the proxy configuration for mapping
const baseDomain = "";
const baseURL = `${baseDomain}`;
const resource = baseURL + "/api/fulltext/";
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

  autocomplete(item, term) {
    return Repository.get(`${resource}/${item}/autocomplete?q=${term}`);
  }, 

  search(item, term) {
    return Repository.get(`${resource}/search/${item}?q=${term}`);
  }, 

  searchWithPagination(queryString, page, perPage) {
    const offset = perPage * page;
    const limit = perPage;
    return Repository.get(`${resource}/search-with-pagination/?q=${encodeURIComponent(queryString)}&offset=${offset}&limit=${limit}`);
  }, 

  getMovieGroupBy(field) {
    return Repository.get(`${resource}/movies/group_by/${field}`);
  }, 
  
  getMovieById(id){
    return Repository.get(`${resource}movies/${id}`);
  },

  getActorById(id){
    return Repository.get(`${resource}actors/${id}`);
  },
  changeConfigFlag(config) {
    return Repository.get(`${resource}/config/${config}`);
  }, 

  getMovieStats(){
    return Repository.get(`${resource}/stats/movies/all`);
  },

  getServiceInfo(){
    return resource;
  }



};
