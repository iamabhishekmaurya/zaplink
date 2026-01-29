// package io.zaplink.media.interceptor;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
// import org.springframework.web.servlet.HandlerInterceptor;
// @Component @RequiredArgsConstructor
// public class ApiVersionInterceptor
//     implements
//     HandlerInterceptor
// {
//     @Value("${spring.application.api.version.header}")
//     private String versionHeader;
//     @Value("${spring.application.api.version.value}")
//     private String versionValue;
//     @Override
//     public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler )
//         throws Exception
//     {
//         // Skip validation for OPTIONS requests (CORS preflight) or Actuator endpoints if needed
//         if ( "OPTIONS".equalsIgnoreCase( request.getMethod() ) )
//         {
//             return true;
//         }
//         String headerValue = request.getHeader( versionHeader );
//         if ( headerValue == null || !headerValue.equals( versionValue ) )
//         {
//             response.sendError( HttpServletResponse.SC_BAD_REQUEST, "Missing or Invalid " + versionHeader + " header" );
//             return false;
//         }
//         return true;
//     }
// }
