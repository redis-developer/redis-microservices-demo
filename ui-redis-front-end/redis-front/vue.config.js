module.exports = {
    devServer: {
        proxy: {
            '^/api/fulltext': {
            target: 'http://localhost:8085/api/1.0/data-streams-to-autocomplete',
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
                target: 'http://localhost:8084/api/1.0/cache-invalidator',
                ws: true,
                changeOrigin: true,
                pathRewrite: {"^/api/caching" : "/"}          
            },        
            '^/api/data-to-streams': {
                target: 'http://localhost:8082/api/1.0/data-streams-producer',
                ws: true,
                changeOrigin: true,
                pathRewrite: {"^/api/data-to-streams" : "/"}          
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