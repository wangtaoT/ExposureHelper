一个很方便对View进行曝光埋点收集的库，不用修改现有布局实现方式，只需在现有代码上做极少量修改即可实现View的曝光埋点。支持判断View是否达到有效曝光面积，支持RecyclerView的线性布局、网格布局、瀑布流布局，横向滑动曝光埋点，支持对指定View进行曝光收集。

###### 使用方式：

1. 将需要采集曝光的View替换为对应的曝光View，库里面提供了三个曝光View ***(ExposureLinearLayout，ExposureFrameLayout，ExposureRelativeLayout)***，若需其他类型的曝光View可以自行让对应View实现IProvideExposureData接口

   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <com.wt.exposurehelper.view.ExposureFrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:tools="http://schemas.android.com/tools"
       android:id="@+id/exposureRoot"
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:orientation="vertical">
       <TextView
           android:id="@+id/tvText"
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           tools:text="TEXT" />
   </com.wt.exposurehelper.view.ExposureFrameLayout>
   ```

2. 为曝光View绑定上对应的曝光数据

   ```kotlin
   //注:这里的data对象最好实现了equals方法
   exposureRoot.exposureBindData = data
   ```

### RecyclerView添加曝光收集
在给RecyclerView设置完adapter后实例化RecyclerViewExposureHelper，必须在设置完adapter后才能实例化的原因是为了能让RecyclerViewExposureHelper在adapter调用更新item方法后可以自动计算曝光数据。实例化时需传递五个参数
* recyclerView 需要收集曝光的RecyclerView
* exposureValidAreaPercent 判定曝光的面积,即大于这个面积才算做曝光,百分制,eg:设置为50 item的面积为200平方,则必须要展示200 * 50% = 100平方及以上才算为曝光
* lifecycleOwner RecyclerView感知此生命周期组件,根据生命周期感知RV可见性,以便自动处理开始曝光和结束曝光,一般情况RV在Activity中传Activity,在Fragment中传Fragment
* mayBeCoveredViewList 可能会遮挡RV的View集合
* exposureStateChangeListener 曝光状态改变监听器
   ```kotlin
   recyclerViewExposureHelper =
            RecyclerViewExposureHelper(
                recyclerView = rvList,
                exposureValidAreaPercent = 50,
                lifecycleOwner = this,
                mayBeCoveredViewList = null,
                exposureStateChangeListener = object : IExposureStateChangeListener<String> {
                    override fun onExposureStateChange(
                        bindExposureData: String,
                        position: Int,
                        inExposure: Boolean
                    ) {
                        Log.i(
                            "ListActivity", "${bindExposureData}${
                                if (inExposure) {
                                    "开始曝光"
                                } else {
                                    "结束曝光"
                                }
                            }"
                        )
                    }
                }
            )
   ```
其他情况：若RecyclerView被嵌套在可滚动控件(eg:ScrollView,NestedScrollView,RecyclerView等)中，将会导致RecyclerViewExposureHelper中持有的RecyclerView不能响应滑动的情况,就必须由外部告知RecyclerView滚动状态然后触发曝光收集。具体做法：给可滚动控件添加滚动监听，滚动监听中调用recyclerViewExposureHelper.onScroll()