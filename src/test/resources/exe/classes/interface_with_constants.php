<?php
interface StatusCodes {
    const OK = 200;
    const NOT_FOUND = 404;
    const SERVER_ERROR = 500;
    
    public function getStatusCode(): int;
}

class Response implements StatusCodes {
    public function getStatusCode(): int {
        return self::OK;
    }
}

$response = new Response();
echo "OK: " . StatusCodes::OK . "\n";
echo "Response: " . $response->getStatusCode() . "\n";
?>