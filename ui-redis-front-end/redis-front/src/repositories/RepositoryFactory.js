import RDBMSRepository from "./RDBMSRepository";
import CacheInvalidatorRepository from "./CacheInvalidatorRepository";
import DataStreamsToGraphRepository from "./DataStreamsToGraphRepository";

import DataStreamsProducerRepository from "./DataStreamsProducerRepository";
import DataStreamsAutoCompleteRepository from "./DataStreamsAutoCompleteRepository";


const repositories = {
  rdbmsRepository: RDBMSRepository,
  cacheInvalidatorRepository: CacheInvalidatorRepository,
  
  dataStreamsToGraphRepository: DataStreamsToGraphRepository,
  dataStreamsProducerRepository: DataStreamsProducerRepository,
  dataStreamsAutoCompleteRepository: DataStreamsAutoCompleteRepository,

};

export const RepositoryFactory = {
  get: name => repositories[name]
};
