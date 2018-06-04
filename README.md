# UserInfo-SinaWeiBo-Appbar
## 项目说明
本项目模仿了新浪微博的个人主页的拖拽刷新效果。使用appbar+viewpager简单的结构。下拉会改变appbar高度，释放时会弹回原始大小。

项目提供丰富的回调监听，可以根据需求自行扩展。

由于使用appbar+viewpager的结构，viewpager中的fragment向上移动时会整体移动viewpager，所以viewpager中的每一页都是滚动同步的。

具体请自行比对新浪微博不同页切换滚动后的位置变化（尤其是头部有位移之后，不同页的滚动位置）。
## 效果
- 下拉刷新效果

![image](https://github.com/gu0-kim/UserInfo-SinaWeiBo-Appbar/blob/master/screen/refresh.gif)

- 不同页面的滚动同步

![image](https://github.com/gu0-kim/UserInfo-SinaWeiBo-Appbar/blob/master/screen/scroll.gif)
## UML图
![image](https://github.com/gu0-kim/UserInfo-SinaWeiBo-Appbar/blob/master/screen/UserInfo-SinaWeibo-AppBar.png)

## 使用方法

具体参见[HomePageFragment](https://github.com/gu0-kim/UserInfo-SinaWeiBo-Appbar/blob/master/app/src/main/java/com/gu/devel/sinaweibo/userinfo/appbar/fragment/HomePageFragment.java)和[MainActivity](https://github.com/gu0-kim/UserInfo-SinaWeiBo-Appbar/blob/master/app/src/main/java/com/gu/devel/sinaweibo/userinfo/appbar/MainActivity.java)

## 拖拽回调


```
/** 界面回调接口 */
interface AppBarUiCallBack {
    /** appbar下拉没有到达"刷新距离"时回调 */
    void onPull(int offset);
    
    /** appbar下拉超过"刷新距离"时回调 */
    void onPullExceedRefreshSize(int exceedSize, int exceedMaxSize);
    
    /** 抬手后appbar回弹回调 */
    void onRebound(int offset);
    
    /** appbar折叠状态 */
    void onCollapsed();
    
    /** appbar打开状态 */
    void onOpen();
    
    /** appbar介于“打开”和“折叠”之间的状态 */
    void onMiddle(int offset);
    
    void startRefreshing();
}
```
不同的状态，根据你的需求自己实现效果。

但如果你不需要这么多回调，空着他就完事儿了！

