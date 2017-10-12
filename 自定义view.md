# 自定义view笔记

## 1.自定义view常用的几个方法调用

onLayout 
onMeasure
onDraw          这三个方法是一般是经常要用到的自上而下依次调用,onLayout一般在自定义组合控件的时候才会用到,自定义view的时候一般用不到,除了以上三个方法还有下面两个:
onFinishInflate　完成填充布局,此时并未回调onMeasure具体参见博客源码
onSizeChange　此方法调用在回调onLayout之前　一般在视图大小发生回调之后进行回调在回调onSizeChange之后回到onLayout方法.　　　

具体内容参考：<http://blog.csdn.net/anhenzhufeng/article/details/72886181>

***

## 2.自定义view中的两个刷新方法的区别##

requestLayout();  这个方法在什么时候会用到?  了解下这个http://blog.csdn.net/a553181867/article/details/51583060,
http://www.jianshu.com/p/687010ccad66(浅谈DecorView与ViewRootImpl两者的区别)


invalidate(); 这个方法在主线程中进行调用,刷新ui

postInvalidate();源码当中调用了handler发送延迟消息到主线程默认延时时间为0,所以主要是在子线程中进行调用刷新

![刷新介绍](http://upload-images.jianshu.io/upload_images/1734948-b4493f7b0234dd69.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

一般来说，如果View确定自身不再适合当前区域，比如说它的LayoutParams发生了改变，需要父布局对其进行重新测量、布局、
绘制这三个流程，往往使用requestLayout。而invalidate则是刷新当前View，使当前View进行重绘，不会进行测量、布局流程，
因此如果View只需要重绘而不需要测量，布局的时候，使用invalidate方法往往比requestLayout方法更高效。

## 3.自定义view的四个构造方法的作用以及区别
http://blog.csdn.net/zhao123h/article/details/52210732(参考这个介绍)









