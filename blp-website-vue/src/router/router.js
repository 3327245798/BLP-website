import App from "components/App";

/* 最后一个参数就是分出来的模块名称, 根据需要定义数量 */
//const home = (r) => require.ensure([], () => r(require("../components/home/index")), "home");
const home=(r)=>require.ensure([], () => r(require("../views/WebsiteIndex.vue")), "home");
const logout = (r) => require.ensure([], () => r(require("../components/home/logout")), "logout");

const WorkArea = (r) => require.ensure([], () => r(require("../components/WorkArea")), "workArea");
const appCase = (r) => require.ensure([], () => r(require("../components/home/AppCase")), "appCase");
const appCaseList = (r) => require.ensure([], () => r(require("../components/home/AppCaseList")), "appCaseList");
const Dashboard = (r) => require.ensure([], () => r(require("../components/home/Dashboard")), "dashboard");
const PreviewLink = (r) => require.ensure([], () => r(require("../components/home/PreviewLink")), "previewlink");
const PreviewTool = (r) => require.ensure([], () => r(require("../components/home/PreviewTool")), "previewtool");


/* const taste = (r) => require.ensure([], () => r(require("../components/home/Taste")), "taste"); */
const GongGaoMore = (r) => require.ensure([], () => r(require("../components/home/GonggaoAndDongTai/GongGaoMore")), "gongGaoMore");
const NoticeDetail = (r) => require.ensure([], () => r(require("../components/home/GonggaoAndDongTai/NoticeDetail")), "noticeDetail");
const Pagination = (r) => require.ensure([], () => r(require("../components/home/GonggaoAndDongTai/Pagination")), "pagination");
const WordPage=(r)=>require.ensure([], () => r(require("../views/WordPage")), "wordPage");
const SentencePage=(r)=>require.ensure([], () => r(require("../views/SentencePage")), "sentencePage");
const CheckSentence=(r)=>require.ensure([], () => r(require("../views/CheckSentence")), "checkSentence");
const CheckWord=(r)=>require.ensure([], () => r(require("../views/CheckWord")), "checkWord");
const WorkerWordPage=(r)=>require.ensure([], () => r(require("../views/WorkerWordPage")), "workerWordPage");
const WorkerSentencePage=(r)=>require.ensure([], () => r(require("../views/WorkerSentencePage")), "workerSentencePage");
const BusinessTypeAppcase=(r)=>require.ensure([], () => r(require("../views/BusinessTypeAppcase")), "businessTypeAppcase");
const TalkWorkCenter=(r)=>require.ensure([], () => r(require("../views/TalkWorkCenter")), "talkWorkCenter");
const TalkDetail=(r)=>require.ensure([], () => r(require("../views/TalkDetail")), "talkDetail");
const TalkMore=(r)=>require.ensure([], () => r(require("../views/TalkMore.vue")), "talkMore");
const PersonCenter=(r)=>require.ensure([], () => r(require("../views/PersonCenter.vue")), "personCenter");
const WordLib=(r)=>require.ensure([], () => r(require("../views/WordLib.vue")), "wordLib");
const SentenceLib=(r)=>require.ensure([], () => r(require("../views/SentenceLib.vue")), "sentenceLib");
const WebsiteIndex=(r)=>require.ensure([], () => r(require("../views/WebsiteIndex.vue")), "websiteIndex");
//展示分析剧本的代码详情
const AnalyseCodeDetail=(r)=>require.ensure([],()=>r(require("../views/analyseCodeDetail.vue")),"analyseCodeDetail");




//路由组件
const CreatePost=(r)=>require.ensure([], () => r(require("../routerComponent/CreatePost.vue")), "createPost");
const CreateWord=(r)=>require.ensure([], () => r(require("../routerComponent/CreateWord.vue")), "createWord");
const  CreateSentence=(r)=>require.ensure([], () => r(require("../routerComponent/CreateSentence.vue")), "createSentence");

export default [
  {
    path: "/",
    component: App, //顶层路由，对应index.html
    children: [
      //二级路由。对应App.vue
       {
           path: '',
           redirect: '/home'
       },
      {
        path: "/home",
        name: "Home",
        component: home,
      },
   

      { path: "/dashboard", name: "Dashboard", component: Dashboard },
     /*  {
        path: "/appcase/:id/:scriptId",
        name: "Taste",
        component: taste,
      }, */
      {
        path: "/logout",
        name: "logout",
        component: logout,
      },
      {
        path: "/appcase/:id",
        name: "AppCase",
        component: appCase,
      },
      {
        path: "/appcase",
        name: "AppCaseList",
        component: appCaseList,
      },
      {
        path: "/gongGaoMore",
        name: "GongGaoMore",
        component: GongGaoMore,
      },
      {
        path: "/noticeDetail",
        name: "NoticeDetail",
        component: NoticeDetail,
      },
      {
        path: "/pagination",
        name: "Pagination",
        component: Pagination,
      },
      {
        path: "/sentencePage2",
        name: "SentencePage",
        component: SentencePage,
      },
      
     
      
    ],
  },
  {
    path: "/wordPage",
    name: "WordPage",
    component: WordPage,
  },
  {
    path: "/sentencePage",
    name: "SentencePage",
    component: SentencePage,
  },
  {
    path: "/checkSentence",
    name: "CheckSentence",
    component: CheckSentence,
  },
  {
    path: "/checkWord",
    name: "CheckWord",
    component: CheckWord,
  },
  {
    path: "/workerSentencePage",
    name: "WorkerSentencePage",
    component: WorkerSentencePage,
  },
  {
    path: "/workerWordPage",
    name: "WorkerWordPage",
    component: WorkerWordPage,
  },
  {
    path: "/appcase/:id/:scriptId",
    name: "WorkArea",
    component: WorkArea,
  },
  {
    path: "/previewlink",
    name: "PreviewLink",
    component: PreviewLink,
  },
  {
    path: "/previewtool",
    name: "PreviewTool",
    component: PreviewTool,
  },
  {
    path: "/businessTypeAppcase",
    name: "BusinessTypeAppcase",
    component: BusinessTypeAppcase,
  },
  {
    path: "/talkWorkCenter",
    name: "TalkWorkCenter",
    component: TalkWorkCenter,
    redirect: '/createPost',
    children:[   
      {
        path: "/createPost",
        name: "CreatePost",
        component: CreatePost,
       
      
      },
      {
        path: "/createWord",
        name: " CreateWord",
        component:  CreateWord,
      },
      {
        path: "/createSentence",
        name: "CreateSentence",
        component: CreateSentence,
      },
    ]
  },
  {
    path: "/talkDetail",
    name: "TalkDetail",
    component: TalkDetail,
  },
  {
    path: "/talkMore",
    name: "TalkMore",
    component: TalkMore,
  },
  {
    path: "/personCenter",
    name: "PersonCenter",
    component: PersonCenter,
  },
  {
    path: "/wordLib",
    name: "WordLib",
    component: WordLib,
  },
  {
    path: "/sentenceLib",
    name: "SentenceLib",
    component: SentenceLib,
  },
  
  {
    path: "/websiteIndex",
    name: "WebsiteIndex",
    component: WebsiteIndex,
    
  },
  {
    path: "/analyseCodeDetail/:content",
    name: "AnalyseCodeDetail",
    component: AnalyseCodeDetail,
  },
];
