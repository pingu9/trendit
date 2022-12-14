<template>
  <q-card flat bordered class="chart-container text-center bg-grey-1">
    <q-card-section>
      <q-btn-toggle
        v-model="type"
        toggle-color="deep-orange-3"
        toggle-text-color="black"
        square
        :options="[
          { label: '일별', value: 'day' },
          { label: '주별', value: 'week' },
          { label: '월별', value: 'month' },
          { label: '연도별', value: 'year' },
        ]"
        class="label-option"
      />
      <!-- CHART -->
      <div id="chartbox">
        <!-- <canvas id="myChart" class="chart" width="400" height="250" /> -->
      </div>
    </q-card-section>
  </q-card>
  <div class="q-pa-md q-gutter-md">
    <!-- <q-card flat bordered class="chart-container text-center bg-grey-1"> -->
    <q-card
      flat
      bordered
      class="side-card q-pa-sm column justify-evenly bg-grey-1"
    >
      <div class="row justify-center">
        <q-icon name="event" size="21px" left />
        <div class="most-news-date-title">
          가장 기사건수가 많았던&nbsp;
          <strong>{{ side_card.word1 }}</strong>
        </div>
      </div>
      <div class="row justify-center most-news-date-content">
        <strong>{{ side_card.date }}</strong>
      </div>
    </q-card>
    <q-card
      flat
      bordered
      class="side-card q-pa-sm column justify-evenly bg-grey-1"
    >
      <div class="row justify-center">
        <q-icon name="trending_up" size="21px" left />
        <div class="most-news-date-title">
          <strong class="rating-title">{{ side_card.word2 }}</strong>
          &nbsp;대비 증감률
        </div>
      </div>
      <div class="row justify-center rating-content">
        <q-icon
          v-if="side_card.percentage > 0"
          name="arrow_drop_up"
          color="red-6"
          size="35px"
        />
        <q-icon v-else name="arrow_drop_down" color="blue-7" size="35px" />
        <div class="rating-content" :style="{ color: updownColor }">
          <strong>{{ Math.abs(side_card.percentage) }}%</strong>
        </div>
      </div>
    </q-card>
  </div>
</template>

<script>
import { ref } from "vue";
import { keywordChartApi } from "boot/stats.js";
import Chart from "chart.js/auto";

export default {
  props: {
    keyword: {
      type: String,
      default: null,
    },
    keyword_id: {
      type: String,
      default: null,
    },
  },
  setup() {
    return {
      data_list: ref(null),
      type: ref("day"),
      side_card: ref({
        word1: "null",
        word2: "null",
        date: "null",
        percentage: "null",
      }),
      updownColor: ref("black"),
    };
  },
  async mounted() {
    await this.drawChart();
  },
  computed: {
    word1() {
      switch (this.type) {
        case "day":
          return "날";
        case "week":
          return "주";
        case "month":
          return "달";
        case "year":
          return "연도";
        default:
          return null;
      }
    },
    word2() {
      switch (this.type) {
        case "day":
          return "전일";
        case "week":
          return "전주";
        case "month":
          return "전월";
        case "year":
          return "전년";
        default:
          return null;
      }
    },
  },
  methods: {
    async loadChartData() {
      await keywordChartApi(
        { type: this.type, keyword: this.keyword_id },
        (response) => {
          this.data_list = response.data.data;
        },
        () => console.warn("failed")
      );
    },
    async drawChart() {
      try {
        const ctx = document.getElementById("myChart");
        ctx.parentNode.removeChild(ctx);
      } catch (e) {}
      const parent = document.getElementById("chartbox");
      const canvas = document.createElement("canvas");
      canvas.setAttribute("id", "myChart");
      canvas.setAttribute("class", "chart");
      canvas.setAttribute("width", "400");
      canvas.setAttribute("height", "250");
      parent.appendChild(canvas);

      const ctx = document.getElementById("myChart");

      await this.loadChartData();

      let max = 0;
      switch (this.type) {
        case "day":
          max = 6;
          break;
        case "week":
          max = 4;
          break;
        case "month":
          max = 11;
          break;
        case "year":
          max = 7;
          break;
        default:
          break;
      }
      let labels = [];
      for (let i = max; i >= 0; i--) {
        let marker = this.getMarker(i);
        labels.push(marker);
      }

      if (this.type == "year") {
        this.data_list = this.data_list.slice(2, 10);
      }

      let data_array = [];
      let max_label = null;
      let max_val = 0;
      if (this.data_list) {
        for (let i = 0; i < this.data_list.length; i++) {
          data_array.push(this.data_list[i]);
          if (this.data_list[i] >= max_val) {
            max_val = this.data_list[i];
            max_label = labels[i];
          }
        }
      }

      console.warn(this.data_list);
      this.side_card.word1 = this.word1;
      this.side_card.word2 = this.word2;
      this.side_card.date = max_label + " (" + max_val + "건)";
      this.side_card.percentage =
        Math.round(
          (this.data_list[this.data_list.length - 1] /
            this.data_list[this.data_list.length - 2] -
            1) *
            10000
        ) / 100;
      if (this.side_card.percentage > 0) {
        this.updownColor = "#F44336";
      } else {
        this.updownColor = "#1E88E5";
      }

      let data = {
        labels: labels,
        datasets: [
          {
            label: this.keyword, // 범례
            data: data_array,
            fill: false,
            borderColor: "#EE6C4D",
            tension: 0.1,
          },
        ],
      };

      const myChart = new Chart(ctx, {
        type: "line",
        data: data,
        options: {
          scales: {
            y: {
              beginAtZero: true,
              title: {
                display: true,
                align: "end",
                color: "#808080",
                font: {
                  size: 12,
                  family: "NanumBarunGothic",
                  weight: 300,
                },
                text: "(단위: 건)",
              },
            },
          },
        },
      });
      myChart;
    },
    getMarker(offset) {
      var date_val = new Date();
      var date_label = "";
      switch (this.type) {
        case "day":
          date_val.setDate(date_val.getDate() - offset - 1);
          date_label = date_val.getMonth() + 1 + "/" + date_val.getDate();
          break;
        case "week":
          if (date_val.getDay() == 1) date_val.setDate(date_val.getDate() - 1);
          if (date_val.getDay() == 0) date_val.setDate(date_val.getDate() - 6);
          else date_val.setDate(date_val.getDate() - (date_val.getDay() - 1));
          date_val.setDate(date_val.getDate() - offset * 7);
          date_label = date_val.getMonth() + 1 + "/" + date_val.getDate();
          break;
        case "month":
          if (date_val.getDate() == 1) date_val.setDate(date_val.getDate() - 1);
          date_val.setMonth(date_val.getMonth() - offset);
          date_label = date_val.getMonth() + 1 + "월";
          break;
        case "year":
          date_val.setFullYear(date_val.getFullYear() - offset);
          date_label = date_val.getFullYear() + "년";
          break;
        default:
          break;
      }
      return date_label;
    },
  },
  watch: {
    type: function () {
      /*
      TODO: watch로 type 선택할 때마다 바꾸는거 오류 해결해야 함
      */
      this.drawChart();
    },
    keyword: function () {
      this.drawChart();
    },
  },
  components: {},
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/earlyaccess/hanna.css");

.chart {
  margin: 10px auto;
}
.chart-container {
  padding: 5px;
  width: 450px;
}
.side-card {
  width: 300px;
  height: 130px;
}

.most-news-date-title {
  font-family: "NanumBarunGothic";
  font-size: 15px;
}
.most-news-date-content {
  font-family: "NanumBarunGothic";
  font-size: 22px;
}
.rating-title {
  font-family: "NanumBarunGothic";
  font-size: 15px;
}
.rating-content {
  font-family: "NanumBarunGothic";
  font-size: 22px;
}
.label-option {
  font-family: "NanumBarunGothic";
  font-size: 21px;
}
</style>
