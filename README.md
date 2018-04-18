# RemindAlarm

## 项目简述
### 1.功能描述：
基于AlaramManager和Service/boardcast实现的小闹钟程序。点击下方的闹钟按钮就可以添加闹钟，长按列表项可以删除闹钟。

### 2.实现细节：
- AlaramManager分别配合Service/boardcast两种方法实现了闹钟响铃功能。
- FloatingActionButton和TimePicker实现闹钟设置。
- RecyclerView实现了闹钟列表的显示。
- SQLite实现了闹钟数据的储存功能。

### 3.不足之处：
- 两种方式：Service和boardcast都没有能实现真正意义上的保活，超过一定时长的闹钟会失效，这个暂时是解决不了了。
- 功能和界面都比较简陋，有很大的优化空间。

## 项目感想
- Android的机型非常的多，很多厂家都对AlaramManager在手机上的实现做了修改，如果要使用AlaramManager来实现某种功能，那么可能需要针对特定的机型，做特定处理。
- RecyclerView的确是实现了完全的解耦，相比于ListView，有更大的灵活性和优化空间。

## 项目链接
- github:https://github.com/tulensayyj/RemindAlarm/blob/master/README.md
