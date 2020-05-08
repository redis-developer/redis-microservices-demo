module.exports = {
    devServer: {
        proxy: {
            '^/api/fulltext': {
            target: 'http://localhost:8085/api/1.0/search-service',
            ws: true,
            changeOrigin: true,
            pathRewrite: {"^/api/fulltext" : "/"}          
            },
            '^/api/legacy': {
                target: 'http://localhost:8081/api/1.0/data-mysql-api',
                ws: true,
                changeOrigin: true,
                pathRewrite: {"^/api/legacy" : "/"}          
            },
            '^/api/caching': {
                target: 'http://localhost:8084/api/1.0/caching',
                ws: true,
                changeOrigin: true,
                pathRewrite: {"^/api/caching" : "/"}          
            },        
            '^/api/db-to-streams-service': {
                target: 'http://localhost:8082/api/1.0/db-to-streams-service',
                ws: true,
                changeOrigin: true,
                pathRewrite: {"^/api/db-to-streams-service" : "/"}          
            },        
            '^/api/graph': {
                target: 'http://localhost:8083//api/1.0/data-streams-to-graph',
                ws: true,
                changeOrigin: true,
                pathRewrite: {"^/api/graph" : "/"}          
            }
        }
    }
}