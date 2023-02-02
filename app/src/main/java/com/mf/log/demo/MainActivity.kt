package com.mf.log.demo

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.mf.log.LogConfig
import com.mf.log.LogManager
import com.mf.log.LogUtils
import com.permissionx.guolindev.PermissionX

class MainActivity : AppCompatActivity(), View.OnClickListener {

    @Volatile
    var isStart = false
    var index = 0
    var bigText = """
        研发进展
        2006年2月9日，国务院发布《国家中长期科学和技术发展规划纲要（2006-2020年）》。大型飞机重大专项被确定为16个重大科技专项之一。8月17日，国务院成立大型飞机重大专项领导小组。 [8] 
        2007年2月26日，国务院召开第170次常务会议，原则通过了《大型飞机方案论证报告》，原则批准大型飞机研制重大科技专项正式立项。8月30日，中央政治局召开第192次常委会，听取并同意国务院大型飞机重大专项领导小组《关于大型飞机重大专项有关情况的汇报》，决定成立大型客机项目筹备组。 [8] 
        2008年5月11日，中国商飞公司在黄浦江畔成立。7月3日，中国商飞公司在上海召开大型客机项目论证动员大会。
        2009年1月6日，中国商飞公司正式发布首个单通道常规布局150座级大型客机机型代号“COMAC919”，简称“C919”。12月21日，中国商飞公司与CFM公司在北京正式签署C919大型客机动力装置战略合作意向书，选定CFM公司研发的LEAP-X1C发动机作为C919大型客机的启动动力装置。12月25日，C919大型客机机头工程样机主体结构在上海正式交付。 [8] 
        2010年12月24日，中国民用航空局正式受理C919大型客机型号合格证申请。 [8] 
        2011年4月18日，C919大型客机首次型号合格审定委员会会议在上海召开，C919飞机研制全面进入正式适航审查阶段。12月9日，C919大型客机项目通过国家级初步设计评审，转入详细设计阶段。 [8] 
        2012年7月31日，《C919飞机专项合格审定计划（PSCP）》在上海签署。12月4日，历时19个月的C919飞机七大部件之一的复合材料后机身部段强度研究静力疲劳试验项目全部完成。 [8] 
        2013年12月30日，C919飞机铁鸟试验台在中国商飞上飞院正式投用，C919项目系统验证工作正式启动。12月31日，C919客机项目首架机头在中航工业成飞民机下线。 [8] 
        2014年5月15日，C919飞机首架机前机身部段在中航工业洪都下线。7月23日，C919飞机首架机平尾部件装配在中国商飞公司总装制造中心浦东基地正式开工。8月1日，C919客机首架机中机身部段在中航工业西飞下线。8月21日，C919客机首架机中后机身部段在中航工业洪都下线。8月29日，C919客机中机身/中央翼、副翼部段在中航工业西飞公司通过适航检查，完成交付。9月19日，C919客机首架机在中国商飞总装制造中心浦东基地开始结构总装。10月30日，C919客机首架机后机身前段在中航工业沈飞民机交付。 [8] 
        2015年2月11日，C919客机首架机后机身后段完成制造并通过适航审查，正式交付中国商飞公司。7月22日，CFM国际公司首台CFMLEAP-1C发动机交付中国商飞公司总装制造中心浦东基地。11月2日，C919客机首架机在浦东基地正式总装下线。这标志着C919首架机的机体大部段对接和机载系统安装工作正式完成，同时，标志着C919客机项目工程发展阶段研制取得了阶段性成果，为下一步首飞奠定了坚实基础。 [8] 
        　中国大飞机C919飞上天空
        　中国大飞机C919飞上天空(3张)
        2016年4月11日，C919客机全机静力试验正式启动。6月，C919水平尾翼智能装配线建设名列工信部智能制造拟入选项目。11月，东方航空成为C919飞机全球首家用户。12月25日，C919飞机首架机交付试飞中心。 [8]  [41] 
        2017年4月18日，C919客机通过首飞放飞评审。4月23日，C919客机完成高速滑行抬前轮试验。5月5日C919在上海浦东机场圆满首飞。11月10日，C919飞机101架机成功从上海转场西安阎良，正式开展后续试飞取证试验工作。 [8] 
        2018年2月26日，中国商飞与华融金融租赁签署30架C919大型客机和20架ARJ21新支线飞机购机协议。3月24日，C919飞机102架机顺利完成第5次试飞。6月6日，C919完成全机静力试验首个极限载荷工况。6月22日，两架C919两地同步试飞。7月12日，C919大型客机102架机转场东营试飞基地。同日，C919大型客机完成2.5g极限载荷静力试验。 [8]  此次转场东营试飞基地的102架机主要承担动力装置、燃油及惰化系统、电源系统、环控系统等地面试验验证和试飞科目。随着第二架试飞飞机转场东营试飞基地，中国商飞公司正式开启C919大型客机“1+M+N”多机场、多区域协同试飞模式。这意味着C919大型客机项目正式开启多地同步试飞模式，未来将接受各种复杂气象条件考验和系列高风险试飞科目挑战。 [9-10]  10月6日，中国第三架C919客机B-001D号机完成喷漆正式对外亮相。 [11]  10月27日，C919大型客机第二架机从山东东营胜利机场起飞，历经2小时6分，平稳降落在南昌瑶湖机场，圆满完成转场飞行任务。 [12]  12月28日，试飞员徐远征、张健伟驾驶着C919飞机103架机，搭载着观察员蔡俊，试飞工程师赖培军、王涛在浦东国际机场的第四跑道上，滑跑、抬头、冲上云霄。在1小时38分钟的飞行中，C919飞机103架机完成了21个试验点。12时45分，C919客机103架机平安降落上海浦东国际机场，圆满完成第一次飞行，标志着共三架C919飞机进入试飞状态。此次飞行后，C919飞机103架机将适时转场西安阎良，开展颤振、空速校准、载荷、操稳和性能等科目的试飞。C919飞机两架试飞机在西安阎良、山东东营和南昌瑶湖开展多地同步试验试飞。另外三架试飞机正按计划进行部装、总装，六架试飞飞机计划2019年全部投入试飞取证工作。 [13-14]  
        C919
        C919(8张)
        2019年7月26日，C919大型客机103架机顺利转场西安阎良，开启新阶段试验试飞任务。7月30日，C919大型客机104架机在上海浦东机场第五跑道完成中、高速滑行试验，滑行总时长1小时39分钟，为首次飞行做好准备。 [15]  8月1日，C919大型客机104架机完成首次试验飞行。8月30日，C919大型客机104架机顺利转场山东东营，开启新阶段试验试飞任务。9月26日，C919首批交付客户飞机零组件开工投产。10月24日，C919大型客机105架机完成首次试验飞行。 [8]  12月5日，C919客机105架机于11时05分从上海浦东机场起飞，12时41分降落南昌瑶湖机场，顺利完成该机首次空中远距离转场飞行。 [16]  2019年12月27日，C919客机106架机于10时15分从浦东机场第四跑道起飞，经过2小时5分钟的飞行，在完成了30个试验点后，于12时20分返航并平稳降落浦东机场，顺利完成其首次飞行任务。至此，C919客机6架试飞飞机已全部投入试飞工作，项目正式进入“6机4地”大强度试飞阶段。 [17] 
        一图带你了解国产大飞机C919
        一图带你了解国产大飞机C919 [67]  
        2020年2月23日，C919大型客机106架机转场东营试飞。3月，C919大型客机在山东东营完成了飞行推力确定试飞，这是C919试飞过程中的关键课目，这一试飞的完成，标志着C919大型客机的取证试飞工作全面提速。 [18]  6月28日，C919客机降落在吐鲁番交河机场。客机在吐鲁番市将开展为期一个月的高温专项飞行试验。 [19]  10月31日，在南昌飞行大会上，C919大型客机进行全球首次动态展示。11月27日，中国民航上海航空器适航审定中心签发C919项目首个型号检查核准书（TIA），C919飞机进入局方审定试飞阶段。 [8]  12月25日，C919大型客机抵达内蒙古呼伦贝尔海拉尔东山机场，全面开启高寒专项试验试飞。 [26] 
        2021年1月，在经历了20天的测试后，C919大型客机高寒试验试飞专项任务取得圆满成功。以验证在极寒条件下飞机各系统和设备的功能和性能是否符合适航条件。该客机于2020年12月25日飞抵内蒙古呼伦贝尔市海拉尔东山机场开展高寒试验试飞专项任务，试验试飞期间，当地最低气温已近-40℃。 [3]  3月1日，中国东方航空与中国商飞正式签署首批5架C919购机合同。 [8]  5月10日至12日举办的中国品牌日活动上，C919大型客机亮相。 [27] 
        2022年1月19日，在2022年上海两会期间，中国商用飞机有限责任公司副总经理、总会计师吴永良表示，国产C919项目仍处于适航取证阶段，预计将于2022年完成交付。“具体的交付将等到取证完成后才具备条件。” [42] 
        2022年，国产大飞机C919尚有大量适航审定任务需要完成，且还面临着美国的出口管制难题。 [43] 
        2022年，首架国产大飞机C919即将正式交付，订单已达850架，预计单价0.8亿美元。 [44] 
        2022年5月8日下午，首架交付机也就是07号飞机在浦东机场第五跑道进行了中、低速滑行测试，这说明，第一架交付给东航的C919客机已经制造完毕，即将首飞。 [46] 
        2022年5月14日6时52分，编号为B-001J的C919大飞机从浦东机场第4跑道起飞，于9时54分安全降落，标志着中国商飞公司即将交付首家用户的首架C919大飞机首次飞行试验圆满完成。在其试验阶段和交付运营后的全生命周期，中再集团旗下中再产险提供了全面的再保险支持保障。 [47]  [51]  7月5日中午，机身和尾翼涂抹着“C919”国产大飞机从郑州机场起飞，这也标志着国产大飞机C919首次在郑州机场完成起降。 [52]  7月7日12时01分，国产大飞机C919在武汉天河机场落地。 [53] 
        2022年7月，桂林两江国际机场迎来了一位“新朋友”——一架机身和尾翼喷涂着“C919”标识的国产大飞机。在机组和地面保障单位的精心“呵护”和精准操作下，飞机承载着满满的祝福与自豪顺利起飞，标志着国产大飞机C919在桂林机场的首次试飞任务圆满结束。 [54] 
        2022年7月12日，C919首航北京大兴国际机场。 [56] 
        2022年7月23日，中国商飞公司官方微信公众号“大飞机”发布消息称，7月19日，在C919大型客机六架试飞机圆满完成全部试飞任务之际，C919大型客机试飞现场联合指挥部阎良战区在陕西渭南机场召开总结大会。会议强调，C919六架试飞机完成全部试飞任务，标志着C919取证工作正式进入收官阶段，开始全力向取证冲锋。 [55] 
        2022年7月24日消息，东航首批引进5架C919国产大飞机，将用于上海至武汉等多条航线。 [56] 
        2022年7月24日消息，C919大飞机六架试飞机已圆满完成全部试飞任务，标志着C919适航取证工作正式进入收官阶段。 [57] 
        2022年8月1日，中国商飞官微宣布，国产大飞机C919完成取证试飞 [59]  。
        2022年8月9日，民航局发布《关于就中国商飞C919型飞机专用条件和豁免征求意见的通知》。 [61] 
        2022年9月7日，国产大飞机C919首次试飞合肥新桥机场 [62]  。
        2022年9月13日，中国商飞两架C919客机从上海浦东飞往北京首都机场，注册号分别为B-001F和B-001J。预计最快9月底完成取证。 [63] 
        2022年9月19日，中国民用航空局将在京为C919颁发为适航证，并计划2022年内向首个客户东航交付首架飞机。 [85]  9月，完成全部适航审定工作 [67]  ，9月29日，获中国民用航空局颁发的型号合格证。 [65]  [67]  10月，据交通发布，东航消息，在10月24日上午召开的中国东航党组扩大会上，东航总经理、党组副书记李养民表示，首架C919飞机将于2022年12月交付东航。 [75]  11月，第十四届中国航展开幕，国产大飞机C919获签300架订单。 [80] 
        2022年9月30日消息，国产C919大型客机于2022年9月完成全部适航审定工作后获中国民用航空局颁发的型号合格证，将于2022年底交付首架飞机。 [68] 
        2022年9月29日，中共中央、国务院对C919大型客机取得型号合格证，向国务院大型飞机重大专项领导小组、中国商用飞机有限责任公司并C919大型客机项目各参研参试参审单位和全体同志致贺电。 [69] 
        总书记：大飞机事业一定要办好
        总书记：大飞机事业一定要办好 [72]  
        2022年9月30日，党和国家领导人习近平、韩正等在北京人民大会堂会见C919大型客机项目团队代表并参观项目成果展览。 [70]  总书记赞扬大家是国家栋梁、英雄功臣，并强调指出，要聚焦关键核心技术，继续合力攻关；要把安全可靠性放在第一位，消除一切安全隐患。总书记叮嘱大家：大飞机事业一定要办好。 [72] 
        2022年11月5日，C919抵达珠海，C919将在第十四届中国航展中进行静态展示和飞行表演。 [76] 
        中国C919大型客机在航展上进行精彩飞行表演。
        中国C919大型客机在航展上进行精彩飞行表演。
        2022年11月8日，中国商用飞机有限责任公司(中国商飞)首次携C919大型客机在第十四届中国国际航空航天博览会亮相，并通过馆内展览、室外静展和飞行展示等多种方式，向公众呈现中国大飞机事业取得的阶段性成就。 [78]  C919国产大飞机在航展上演大仰角起飞、45度盘旋机动等，单价6.5亿元。 [79]  11月17日11:07，国产大飞机C919（B-001F）从上海浦东机场起飞，计划于14:00到达成都双流国际机场。这是国产大飞机C919首次飞抵成都。 [82] 
        2022年11月18日，C919（B-001F）从成都双流机场起飞，正首飞高高原机场，目的地是阿坝红原机场。 [83] 
        2022年11月20日，中国商飞国产大飞机C919（B001F）从浦东国际机场起飞，平稳降落在扬州泰州国际机场跑道。 [84] 
        2022年11月29日，C919 飞机 T5 测试总结暨颁发 AEG 评审报告活动在中国商飞客服中心举行。T5 测试完成后，包括中国商飞客服中心飞行教员在内的 15 名飞行员取得 C919 飞机型别资质，这也标志着 C919 飞机即将交付运行。 [86]  同日，民航华东地区管理局向中国商飞公司颁发C919飞机生产许可证。 [87-88]  
        2022年12月1日，国产大飞机C919（B-001F）于12月1日8:30 从上海浦东机场起飞，11:31到达宁夏固原六盘山机场，全程飞行时长3小时1分钟。这是C919首次飞抵固原六盘山机场，该机场为高原机场（海拔1745米）。 [89] 
        2022年12月23日，中国东方航空编号为B-919A的全球首架C919客机飞抵扬州泰州国际机场，进行飞行训练。 [96] 
        2022年12月24日消息，东航接收的全球首架C919飞机，将从12月26日开始100小时的验证飞行。验证飞行将全面检验东航各系统为迎接C919商业运行所开展的准备是否可靠，能否向旅客提供安全舒适的国产大飞机航程，以此为即将开启的商业运行完成最后冲刺。 [97-98]  
        2022年12月26日，全球首架C919开启100小时验证飞行。 [99]  飞常准App数据显示，首段飞行计划将于26日13:00从上海虹桥机场起飞前往北京首都机场，17:25将从北京首都机场返回上海虹桥机场。预计往返飞行时长需3.5小时。这也是东航涂装的C919首架交付机首次抵达北京。 [100]  12月27日13时21分许，航班号为 MU7803 的东航全球首架 C919 国产大飞机，首次降落成都天府国际机场。 [101]  12月30日，伴随着东方航空MU7805航班的落地，一架C919飞机从上海虹桥国际机场飞抵西安咸阳国际机场，完成了西安验证飞行，为飞机后续投入商业载客飞行奠定了基础。 [102] 
        2023年1月1日，中国东方航空全球首架C919大型客机测试飞行，首班验证飞行的航班号为MU7809，从上海虹桥国际机场飞往北京大兴国际机场 [104]  ，于10时14分顺利抵达北京。 [105] 
        2023年1月2日11时42分，伴随着MU7807航班的落地，中国东航全球首架国产大飞机C919从上海虹桥机场飞抵海口美兰国际机场，穿过象征民航最高礼仪的“水门”，正式开始在海口站的验证飞行工作。 [106] 
        首架交付
        2022年12月9日，中国东方航空作为国产大飞机C919的全球首家用户，正式接收编号为B-919A的全球首架飞机，迈出市场运营的关键“第一步”，并有望最早于2023年春投入商业载客运营。 [90-91]  C919首架机交付是继C919获颁中国民航局型号合格证后，我国大飞机事业征程上的又一重要里程碑，意味着历经几代人的努力，我国民航运输市场将首次拥有中国自主研发的喷气式干线飞机。 [92] 
        此次交付中国东方航空的C919大型客机有着特殊的涂装和“身份证号”。除了东航的标准化涂装之外，这架飞机在机身前部印有“全球首架”的“中国印”标识和相对应的英文。飞机注册号选用B-919A，B代表飞机的中国国籍，919和型号名称契合，A则有首架之意，突显这一全球新机型、全球首架的非凡意义。 [93] 
        △ 全球首架C919标识
        △ 首架C919飞机注册号
        △ 全球首架C919照片
        △ 全球首架C919标识
        研制背景编辑 播报
        （中国飞机史上）大飞机重大专项是党中央、国务院建设创新型国家，提高中国自主创新能力和增强国家核心竞争力的重大战略决策，是《国家中长期科学与技术发展规划纲要（2006-2020）》确定的16个重大专项之一。让中国的大飞机飞上蓝天，是国家的意志，人民的意志。 [38] 
        中国商用飞机有限责任公司成立于2008年，总部设在上海，是实施国家大飞机重大专项中大型客机项目的主体，员工8300多人，确定了“一个总部，六大中心”的布局。设计研发中心承担了中国首次自主研制的C919客机、ARJ21新支线飞机的工程设计任务和技术抓总责任。 [38] 
        COMAC是C919的主制造商中国商用飞机有限责任公司的英文名称简写，“C”既是“COMAC”的第一个字母，也是中国的英文名称“CHINA”的第一个字母，体现了大型客机是国家的意志、人民的期望。 [2] 
        发动机
        2011年6月20日，中国商用飞机有限责任公司及其所属上海飞机制造有限公司，在巴黎航展开幕当天与CFM国际公司正式签署C919大型客机项目推进系统合同，选择CFM国际公司作为C919项目国外的推进系统供应商，选定CFM国际公司研发的LEAP-X1C发动机为C919大型客机启动动力装置。 [4] 
        CFM国际公司总裁兼CEO让·保罗·班格和中国商飞公司总经理金壮龙出席合同签署仪式。这是中国商飞公司正在研制的C919大型客机签署的第一份机载系统供应商合同。按照计划，中国商飞公司将于2011年完成C919大型客机机体结构、主要机载系统供应商合同的签署工作。 [4] 
        C919大型客机是中国商飞公司正在研制的150座级单通道中短程商用干线飞机，计划于2014年首飞、2016年首架交付用户运营。LEAP-X1C发动机是CFM国际公司研发的新一代发动机。该发动机是在CFM56系列发动机的架构基础上，针对下一代飞机进行改进的产品，对提高飞机的安全性、经济性和环保性有着积极的作用。 [4] 
        CFM国际公司是全球最主要的商用飞机发动机制造商之一，是法国赛峰集团下属的斯奈克玛公司与美国通用电气公司的合资公司。CFM国际公司已向全球500多家飞机运营商提供了20,000多个发动机。 [4] 
        2009年12月21日，在中国国务院总理温家宝和法国总理弗朗索瓦·菲永共同见证下，中国商飞公司与CFM国际公司在北京签署了战略合作意向书。C919大型客机项目推进系统合同的签署，预示着双方的合作进入一个新的阶段。 [4] 
        具体原则
        具有完全自主知识产权。
        项目初期采购部分国外系统设备，鼓励国外供应商在中国发展。逐步形成中国民机产业。
        满足2020年国际要求（如污染排放，噪声等）。
        确保安全性、突出经济性、提高可靠性、改善舒适性、强调环保性。
        掌握和了解市场与客户的需求，减阻、减重、减排，全面优于竞争机，直接使用成本（DOC）降低10%。
        采用国际标准，以国内销售为主，打入国际市场。
        强化战略合作。要按照“主供应商-供应商”模式，深化国际国内合作，风险共担、利益共享，形成大型客机的国际国内供应商体系。 [38] 
        合作单位
        派克汉尼汾公司（Parker Hannifin Corp.）：燃油、油箱惰化和液压系统供应商。
        CFM国际公司（CFM International）：发动机供应商 [5]  。
        霍尼韦尔国际公司（Honeywell International Inc.）：电传飞控系统。
        美国联合技术航空系统公司（United Technology Company）：照明系统（包括整个外部照明、驾驶舱、机舱、货舱照明、紧急照明和客舱LED照明等）、除冰和风挡雨刮系统以及自动油门台和驾驶舱控制系统、防火系统。 [6] 
        美国汉胜公司（UTX’s Hamilton Sundstrand Corp）：电源系统、飞机驾驶员控制系统和防火及过热保护系统。 [7] 
        润贝航科：包括航空化学品、航空原材料及消耗品。 [66] 
        性能数据编辑 播报
        设计技术
        1.采用先进气动布局和新一代超临界机翼等先进气动力设计技术，达到比现役同类飞机更好的巡航气动效率，并与十年后市场中的竞争机具有相当的巡航气动效率；
        2.采用先进的发动机以降低油耗、噪声和排放；
        3.采用先进的结构设计技术和较大比例的先进金属材料和复合材料，减轻飞机的结构重量；
        4.采用先进的电传操纵和主动控制技术，提高飞机综合性能，改善人为因素和舒适性；
        5.采用先进的综合航电技术，减轻飞行员负担、提高导航性能、改善人机界面；
        6.采用先进客舱综合设计技术，提高客舱舒适性；
        7.采用先进的维修理论、技术和方法，降低维修成本。 [28] 
        国产大型客机C919首架机总装下线
        国产大型客机C919首架机总装下线
        设计特点
        C919
        C919
        C919客机属中短途商用机，实际总长38米，翼展35.8米，高度12米，其基本型布局为168座。标准航程为4075公里，最大航程为5555公里，经济寿命达9万飞行小时。 [29] 
        在使用材料上，C919将采用大量的先进复合材料、先进的铝锂合金等，其中复合材料使用量将达到20%，再通过飞机内部结构的细节设计，把飞机重量往下压缩，另外，C919将会使用占全机结构重量20-30%的国产铝合金、钛合金及钢等材料，充分体现了C919大型客机带动国内基础工业的能力与未来趋势。 [29]  同时，由于大量采用复合材料，较国外同类型飞机80分贝的机舱噪音，C919机舱内噪音可望降到60分贝以下。 [30] 
        在减排方面，C919将是一款绿色排放、适应环保要求的先进飞机，通过环保的设计理念，有望将飞机碳排放量较同类飞机降低50%。 [30] 
        C919采用四面式风挡。该项技术是国际上先进的工艺技术，干线客机中只有最新的波音787采用，它的风挡面积大，视野开阔，由于开口相对少，简化了机身加工工业，减少了飞机头部气动阻力。 [30] 
        客舱布局
        C919高密度客舱布局客机模型亮相北京航展
        C919高密度客舱布局客机模型亮相北京航展
        C919共有全经济级、混合级、高密度级三种客舱布置构型，全经济级为168座；混合级156座，公务舱3排12座，经济舱144座；高密度级180座，公务舱每排4座，经济舱每排6座；服务员座椅共4座，前服务区2座，后服务区2座。 [39] 
        用户订单编辑 播报
        2015年11月2日完成总装下线。累计28家客户815架订单。 [60] 
        2018年2月26日，中国华融资产管理股份有限公司旗下控股子公司华融金融租赁股份有限公司与中国商用飞机有限责任公司在北京签署30架C919大型客机协议。截至2018年2月底，C919大型客机国内外用户达到28家，订单总数达到815架。 [20] 
        2018年3月6日下午，十三届全国人大一次会议上海代表团举行全团会议并对媒体开放，国产飞机C919总设计师吴光辉代表在回答新华社记者提问时说，C919已取得国际国内28个用户的815架订单。 [21]  其中国外订单34架，总订单金额超5000亿元 [50]  。
        2022年5月10日晚间，中国东航披露定增公告，拟募资150亿元用于引进38架飞机等事项，其中包括国产大飞机C919、支线客机ARJ21-700共计28架，其余为空客和波音飞机。公告中透露了国产大飞机C919的单价。数据显示，C919的单价为0.99亿美元，折合人民币为6.53亿元。 [49] 
        2022年5月23日消息，中国商飞曾发布市场预测年报，预计到2039年，中国商飞旗下的C919和ARJ21机型市场总规模可达7320亿美元，约合人民币4万亿元。 [50] 
        据路透社当地时间2022年10月1日报道，尼日利亚航空部长哈迪·斯瑞卡（Hadi Sirika）当天表示，尼日利亚将考虑购买中国的C919客机。
        2022年10月2日观察者网消息，C919飞机已有28家客户，累计订单815架 [71]  。
        2022年11月，国产大飞机C919在第十四届中国国际航空航天博览会上收获300架新订单，分别来自于国银金租、工银金租、建信金租、交银金租、招银金租、浦银租赁和苏银金租七家租赁公司。 [81] 
        贡献单位编辑 播报
        参与研制的高校
        清华大学
        清华大学参与了飞机设计的主要方向，包括气动总体、结构强度、航电、飞控和液压系统等。 [31] 
        南京航空航天大学
        南京航空航天大学联合工程队参与了大飞机项目的总体、气动、强度、材料、航电、飞控、动力、环控、防冰、四性、适航等内容的论证工作。 [32] 
        哈尔滨工业大学
        哈尔滨工业大学为C919飞机“起飞”做出了重要贡献。其中包括C919飞机中央翼复合材料后梁大开口补强设计技术研究、C919飞机铝合金机身激光焊接技术及装备研究。 [33] 
        上海交通大学
        在C919客机研发设计的过程中，上海交通大学在总体、结构、制造、材料、航电、人因等专业或领域发挥了重要作用，研究团队分布在交大机械与动力工程学院、材料科学与工程学院、船舶海洋与建筑工程学院、电子信息与电气工程学院、航空航天学院、化学化工学院等多个学院。 [34] 
        复旦大学
        复旦大学航空航天系艾剑良教授、杨爱明副教授、孙刚教授，光源与照明工程系林燕丹教授、孙耀杰教授，计算机科学技术学院杨卫东教授等有幸承担了其中的相关研究课题，助力国产大飞机上天。 [35] 
        合肥工业大学
        合肥工业大学电气与自动化工程学院教授段泽民及其科研团队参与了该机型雷电防护方面的研制工作。 [22] 
        天津大学
        座舱环境控制系统，即飞机的“呼吸系统”具体空气分配设计方案的数值仿真和优化设计，由天津大学团队完成。 [23] 
        西北工业大学
        2008年5月起的半年内，共有16名教师入选中国商飞大型客机联合工程队，校内有12名教师参与JET-B方案研究，11名教师参与BWB布局方案研究，12名教师参与大型客机翼型/机翼设计，其中教授18名，副教授16名。从项目立项起，西工大共有82位师生参与了C919飞机的动力系统、控制系统、结构设计、航电等在内的课题攻关。 [36] 
        大连理工大学
        大连理工大学工程力学系工业装备结构分析国家重点实验室白瑞祥团队先后承担并圆满完成了C919加筋壁板结构、垂尾盒段结构以及尾翼前缘抗鸟撞等结构的承载能力检测和力学性能计算评估等5项课题。 [24] 
        北京航空航天大学
        北京航空航天大学在总体、结构、材料、气动、适航等领域发挥了重要作用，包括参与C919的气动弹性科研攻关，形成了气动弹性专业的技术方案；在国内首次自主全过程地完成了大型飞机热气防冰系统的设计，开发研制了一套具有自主知识产权的飞机防冰系统设计与评估软件；刘沛清团队完成了多项技术攻关课题，其中包括高亚声速机翼设计等；焦宗夏团队为C919研制了国内第一台C919大型客机多轮刹车系统的便携式自动测试系统；熊峻江团队开展了“民机金属结构疲劳评定体系方案”研究；张广军院士团队研制的航空装备飞行性能动态测试站等等。 [37] 
        武汉理工大学
        武汉理工大学有一支由10余名教师领衔、共60余名师生组成的研究团队，他们参与了C919大飞机复合材料选材以及结构强度设计与分析工作。 [25] 
        中南大学
        大飞机的机轮刹车系统由中南大学独家提供，并由中南大学校控股公司制造完成。 [37] 
        燕山大学
        燕山大学参与了轴承相关的项目。 [37] 
        中国民航大学
        中国民航大学为客机C919技术方案联合论证、安全性技术评估管理与文件体系、适航工程体系、供应商管理体系和客户服务技术体系等方面提供重要支持。 [37] 
        经济价值编辑 播报
        2022年5月10日晚间，中国东航披露定增公告，透露了国产大飞机C919的单价。数据显示，C919的单价为0.99亿美元，折合人民币为6.53亿元。 [45] 
        相关荣誉编辑 播报
        2022年11月，中国商飞和航空工业集团因在C919项目试飞取证过程中的密切协作获第十五届航空航天月桂奖“携手合作奖”。 [77] 
        2022年12月，入选2022年度央企十大国之重器榜单。 [103] 
    """.trimIndent()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btn_log_v).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_d).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_i).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_w).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_e).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_alot).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_test_start).setOnClickListener(this)
        findViewById<Button>(R.id.btn_log_test_end).setOnClickListener(this)
        findViewById<Button>(R.id.btn_backup).setOnClickListener(this)
        LogUtils.init(
            this, LogConfig.Builder()
                .logBasePath(LogUtils.getSdPath() + "/mflog")
                .logDir("main")
                .isEnableThreadInfo(true)
                .isEnableStackTrace(true)
                .isLogToFileByXLog(true)
                .isLogToFileByMars(true)
                .addCleanConfig(
                    LogConfig.CleanConfig.Builder()
                        .countLimit(3)
                        .checkInterval(5 * 1000)
                        .logDir(LogUtils.getSdPath() + "/mflog/test/")
                        .build()
                )
                .build()
        )
        requestPermissions()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_log_v -> LogUtils.v("this is verbose msg, 这是冗余日志")
            R.id.btn_log_i -> LogUtils.i("this is info msg, 这是信息日志")
            R.id.btn_log_w -> LogUtils.w("this is warn msg, 这是警告日志")
            R.id.btn_log_e -> LogUtils.e("this is error msg, 这是错误日志", NullPointerException())
            R.id.btn_log_alot -> LogUtils.i(bigText)
            R.id.btn_log_test_start -> {
                isStart = true;
                index = 0
                Thread {
                    while (isStart) {
                        LogUtils.i("log info test, 信息日志测试 $index")
                        LogUtils.e("log error test, 错误日志测试 $index", NullPointerException())
                        index++
                        Thread.sleep(10)
                    }
                }.start()
            }
            R.id.btn_log_test_end -> {
                isStart = false
            }
            R.id.btn_backup -> {
                LogManager.getInstance().backupLog()
            }
            else -> LogUtils.d("this is debug msg, 这是调试日志")
        }
    }

    private fun requestPermissions() {
        val permissionList = mutableListOf(
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            permissionList.add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
        PermissionX.init(this)
            .permissions(permissionList)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
                if (beforeRequest) {
                    scope.showRequestReasonDialog(
                        deniedList,
                        "即将申请的权限是程序必须依赖的权限", "我已明白"
                    )
                } else {
                    scope.showRequestReasonDialog(
                        deniedList,
                        "即将重新申请的权限是程序必须依赖的权限", "我已明白"
                    )
                }
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "您需要去应用程序设置当中手动开启权限", "去设置"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                LogUtils.d(
                    "allGranted:$allGranted grantedList:$grantedList " +
                            "deniedList:$deniedList"
                )
                if (allGranted) {
                    onAllPermissionsGranted()
                }
            }
    }

    private fun onAllPermissionsGranted() {
    }
}