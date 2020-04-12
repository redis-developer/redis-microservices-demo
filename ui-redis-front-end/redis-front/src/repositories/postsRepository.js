import Repository from "./Repository";

// You can use your own logic to set your local or production domain
const baseDomain = "https://jsonplaceholder.typicode.com";
// The base URL is empty this time due we are using the jsonplaceholder API
const baseURL = `${baseDomain}`;

const resource = baseURL + "/posts";
export default {
  get() {
    return Repository.get(`${resource}`);
  },

  getPost(postId) {
    return Repository.get(`${resource}/${postId}`);
  },

  createPost(payload) {
    return Repository.post(`${resource}`, payload);
  },

  getServiceInfo(){
    return resource;
  }

};
