<template>
  <q-card class="company-list q-pa-md" style="margin: 20px auto">
    <q-card-section class="card-title">
      <div class="title">기업 리스트</div>
    </q-card-section>

    <!-- 검색창 -->
    <q-card-section class="row justify-center q-py-xs">
      <q-input
        class="search-bar"
        v-model="search"
        label="검색"
        outlined
        dense
        bottom-slots
        @keydown.enter="doSearch()"
      >
        <template v-slot:after>
          <q-btn push unelevated color="white" @click="doSearch()">
            <q-icon color="grey" name="search" />
          </q-btn>
        </template>
      </q-input>
    </q-card-section>
    <q-separator inset />
    <div class="flex q-pa-lg justify-around">
      <template v-for="(item, index) of company_list" :key="index">
        <q-card class="company-card q-my-md">
          <q-card-section class="company-title row justify-between">
            <div>{{ item.companyName }}</div>
            <q-icon
              name="add_home"
              class="cursor-pointer"
              @click="goToLink(item.companyLink)"
            />
          </q-card-section>
          <q-separator />
          <q-card-section class="company-content">
            <div>업종: {{ content_filter(item.companyCategory) }}</div>
            <div>대표자: {{ content_filter(item.companyRepresentative) }}</div>
          </q-card-section>
        </q-card>
      </template>
    </div>
    <div class="q-pa-lg flex flex-center">
      <q-pagination v-model="current" :max="max_page" color="blue-6" input />
    </div>
  </q-card>
  <q-dialog v-model="dialog">
    <q-card>
      <q-card-section>
        <div>검색어를 입력해주세요.</div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import { searchCompanyApi } from "boot/company.js";
import { ref } from "vue";
export default {
  setup() {
    return {
      search: ref(""),
      current: ref(1),
      max_page: ref(99),

      dialog: ref(false),

      company_list: ref([]),
    };
  },
  created() {
    this.loadCompanyList();
  },
  methods: {
    async loadCompanyList() {
      await searchCompanyApi(
        { companyName: this.search, page: this.current },
        (response) => {
          this.company_list = response.data.data.content;
          this.max_page = response.data.data.totalPages;
        },
        () => console.warn("failed")
      );
    },
    async doSearch() {
      this.current = 1;
      await this.loadCompanyList();
    },

    goToLink: function (companyLink) {
      window.open(companyLink);
    },
  },
  watch: {
    current: function () {
      this.loadCompanyList();
    },
  },
  computed: {
    content_filter() {
      return function (content) {
        if (content.length <= 15) return content;
        return content.substring(0, 12) + "...";
      };
    },
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap");
@import url("https://hangeul.pstatic.net/hangeul_static/css/nanum-barun-gothic.css");
@import url("https://fonts.googleapis.com/earlyaccess/hanna.css");

.title {
  font-family: "NanumBarunGothicBold";
  font-size: 17px;
}

.search-bar {
  width: 500px;
}

.company-list {
  height: 1000px;
  max-width: 1050px;
  width: 100%;
  background-color: #ffffff;
  margin-top: 10px;
  margin-bottom: 50px;
}
.company-card {
  height: 200px;
  width: 230px;
  padding: 15px 0 15px 15px;
}

.company-title {
  font-family: "NanumBarunGothicBold";
  font-size: 20px;
}
.company-content {
  font-family: "NanumBarunGothic";
  font-size: 13px;
  line-height: 250%;
}
.add-home {
  position: absolute;
  margin-left: 180px;
  margin-top: 20px;
}
</style>
