package com.neuedu.common.interceptor;

/**
 * 全局异常
 */
//@Component
//public class exception implements HandlerExceptionResolver {
//    @Override
//    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
//
//        打印对战信息（调试方便，上线删除）
//        e.printStackTrace();
//        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
//        modelAndView.addObject("status", Responsecode.ERROR);
//        modelAndView.addObject("msg","接口调用出错");
//        modelAndView.addObject("data",e.toString());
//        return null;
//    }
//}
