@Component
public class RequestContextInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check for User-Agent, headers, or custom logic to decide the client type
        String userAgent = request.getHeader("User-Agent");
        
        // Assume any user-agent containing "MobileApp" is a mobile client
        if (userAgent != null && userAgent.contains("MobileApp")) {
            request.setAttribute("clientType", "mobile");
        } else {
            request.setAttribute("clientType", "web");
        }
        
        return true;
    }
}
