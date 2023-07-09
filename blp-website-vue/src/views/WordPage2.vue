<template>
  <div class="wordpage-container">
    <div class="wordku-container">
      <a-cascader
        :options="options"
        :load-data="loadData"
        placeholder="Please select"
        change-on-select
        @change="onChange"
      />
      <a-list item-layout="horizontal" :data-source="words">
        <a-list-item slot="renderItem" slot-scope="item, index">
          <a-list-item-meta
            description="Ant Design, a design language for background applications, is refined by Ant UED Team"
          >
            <a slot="title" href="https://www.antdv.com/">{{ item.title }}</a>
            <a-avatar
              slot="avatar"
              src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
            />
          </a-list-item-meta>
        </a-list-item>
      </a-list>
    </div>
    <div class="other-container"></div>
  </div>
</template>
<script>
import WordService from "@/service/word_service";
const wordService = new WordService();
export default {
  name: "WordPage",
  components: {},
  data() {
    return {
      options: [],
      words: [
        {
          title: "Ant Design Title 1",
        },
        {
          title: "Ant Design Title 2",
        },
        {
          title: "Ant Design Title 3",
        },
        {
          title: "Ant Design Title 4",
        },
      ],
    };
  },
  computed: {},
  beforeCreate() {},
  created() {
    wordService.getAllOrganization(
      {
        //objId: "",
      },
      {
        onSuccess: (model, fModel, json) => {
          console.log(json);
          // for(var i=0;i<json.data.rows.length;i++){
          //   let name = json.data.rows[i].name;
          //   let id = json.data.rows[i].organizationID;
          //   this.options[i] = {value: id, label: name};
          //   // console.log(json.data.rows[i].name);
          //   console.log(this.options.value);
          // }
          this.options = json.data.rows.map((val) => {
            return {
              value:val.homeBandID,
              //value: val.organizationID,
              label: val.name,
              isLeaf: false,
            };
          });

          //console.log(this.options);
        },
        onFail: (msg, json) => {
          console.log(msg, json);
        },
      }
    );
  },
  watch: {},
  methods: {
    onChange(value) {
      console.log(value);
    },
    loadData(selectedOptions) {
      const targetOption = selectedOptions[selectedOptions.length - 1];
      targetOption.loading = true;
      //console.log(selectedOptions);
      //console.log(targetOption.value);
      wordService.getFirstBandsByOrganizationId(
        {
          organizationId: targetOption.value,//传过来的是所选机构的homeBandID
        },
        {
          onSuccess: (model, fModel, json) => {
            console.log(json);
            targetOption.loading = false;
            targetOption.children = json.data.rows.map((val) => {
              return {
                value: val.bandID,
                label: val.name,
              };
            });
            this.options = [...this.options];
          },
          onFail: (msg, json) => {
            console.log(msg, json);
          },
        }
      );
    },
  },
};
</script>
<style lang="scss">
.wordpage-container {
  height: 100%;
  display: flex;
  .wordku-container {
    flex: 1;
    position: relative;
    background: #fff;
    height: 100%;
    padding-left: 30px;
    padding-top: 10px;
  }
  /* .other-container {
    background: #f7f9fc;
    min-width: 30rem;
  }
 */
 }
</style>