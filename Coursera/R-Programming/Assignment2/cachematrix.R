## Put comments here that give an overall description of what your
## functions do

## Write a short comment describing this function

makeCacheMatrix <- function(x = matrix()) {
        # Initialize the inverse matriz
        inv <- NULL
    
        # Set the value of the matrix
        set <- function(y) {
            x <<- y
            inv <<- NULL
        }
        
        # Get the value of the matrix
        get <- function() x
        
        # Set the inverse of the matrix
        setinv <- function(solve) inv <<- solve
        
        # Get the inverse of the matrix
        getinv <- function() inv
        
        # Return the special vector
        list(set = set, get = get,
             setinv = setinv,
             getinv = getinv)
}


## Write a short comment describing this function

cacheSolve <- function(x, ...) {
        # Get the inverse matrix of x
        inv <- x$getinv()
        
        # Check if the inverse has already calculed
        if(!is.null(inv)) {
            message("getting cached inverse matrix")
            return(inv)
        }
        
        # If not, calculates the inverse of the data
        data <- x$get()
        inv <- solve(data)
        
        # Set the value of the inverse in the cache
        x$setinv(inv)
        
        ## Return a matrix that is the inverse of 'x'
        inv
}
