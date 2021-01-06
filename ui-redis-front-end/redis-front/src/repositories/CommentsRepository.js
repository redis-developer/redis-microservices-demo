import Repository from "./Repository";

// look in the proxy configuration for mapping
const baseDomain = "";
const baseURL = `${baseDomain}`;
const resource = baseURL + "/api/comments";
export default {


  search(item, term) {
    return Repository.get(`${resource}/search/${item}?q=${term}`);
  }, 


  getCommentById(id){
    return Repository.get(`${resource}/${id}`);
  },

  deleteCommentById(id){
    return Repository.delete(`${resource}/${id}`);
  },

  getMovieComment(id){
    return Repository.get(`${resource}/movie/${id}`);
  },

  saveNewComment(comment){
    return Repository.post(`${resource}/movie/${comment.movie_id}`, comment);
  },

  getServiceInfo(){
    return resource;
  }



};
