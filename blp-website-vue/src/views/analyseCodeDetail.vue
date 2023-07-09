<template>
  <div class="analyse-detail"> 
  <a-page-header
    style="border: 1px solid rgb(235, 237, 240)"
    :title="msg.draft.name"
    sub-title="之固件代码"
    @back="() => null"
  />
  <a-collapse >
      <a-collapse-panel   
      v-for="(item,index) in msg.analyseList" 
      :key="index" 
      :header="item.content">
        <highlightjs
        autodetect
        :code="item.unitCode"
    />
      </a-collapse-panel>
    </a-collapse> 
    
  </div>
</template>
<script>
import 'highlight.js/styles/stackoverflow-dark.css';// 可以切换其它样式风格，例如黑色主题
import 'highlight.js/lib/common';
import hljsVuePlugin from "@highlightjs/vue-plugin";
export default {
data(){
    return{
msg:"这里是分析剧本里每句话的详情页面",
activeKey:['0']

    }
},
 components: {
        highlightjs: hljsVuePlugin.component
    },
created() {
    
   let msgStr=this.$route.query.content
    this.msg=JSON.parse(msgStr)
    console.log(this.msg);
    
  },
  watch(){},
  mounted(){
    
  },
computed(){},
mdethods:{
    
},
}

</script>

<style>
.analyse-detail{
    margin: 0 auto;  
}
.recordInfo{
    background-color:red;
}
</style>