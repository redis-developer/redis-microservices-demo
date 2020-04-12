import PostsRepository from "./postsRepository";
import RDBMSRepository from "./RDBMSRepository";
import CacheInvalidatorRepository from "./CacheInvalidatorRepository";
import DataStreamsToGraphRepository from "./DataStreamsToGraphRepository";

import DataStreamsProducerRepository from "./DataStreamsProducerRepository";
import DataStreamsAutoCompleteRepository from "./DataStreamsAutoCompleteRepository";


const repositories = {
  posts: PostsRepository,
  rdbmsRepository: RDBMSRepository,
  cacheInvalidatorRepository: CacheInvalidatorRepository,
  dataStreamsToGraphRepository: DataStreamsToGraphRepository,

  dataStreamsProducerRepository: DataStreamsProducerRepository,
  dataStreamsAutoCompleteRepository: DataStreamsAutoCompleteRepository,

  // other repositories ...
};

export const RepositoryFactory = {
  get: name => repositories[name]
};
