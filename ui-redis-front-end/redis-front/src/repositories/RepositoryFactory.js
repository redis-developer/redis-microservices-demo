import RDBMSRepository from "./RDBMSRepository";
import CacheInvalidatorRepository from "./CacheInvalidatorRepository";
import DataStreamsToGraphRepository from "./DataStreamsToGraphRepository";

import DataStreamsProducerRepository from "./DataStreamsProducerRepository";
import DataStreamsRedisHashSyncRepository from "./DataStreamsRedisHashSyncRepository";


const repositories = {
  rdbmsRepository: RDBMSRepository,
  cacheInvalidatorRepository: CacheInvalidatorRepository,
  
  dataStreamsToGraphRepository: DataStreamsToGraphRepository,
  dataStreamsProducerRepository: DataStreamsProducerRepository,
  dataStreamsRedisHashSyncRepository: DataStreamsRedisHashSyncRepository,

};

export const RepositoryFactory = {
  get: name => repositories[name]
};
