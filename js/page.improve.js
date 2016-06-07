(function ($, window, document, undefined) {
  var MP = window.MP || {};

  var testData = {
    "query" : "一天的工作又开始了，忙并快乐着[偷笑]",
    "entrys" : [{
      "text" : "我闻到了，黑木耳的味道",
      "img" : "http://ww2.sinaimg.cn/large/491fdd54jw1e8zwjkqwcvj218g0xc123.jpg" 
    },{
      "text" : "红宝石，蓝宝石，啊，一碰就碎",
      "img" : "http://xinlingzu.lofter.com/post/1d33aff_b08b6d1"
    }]
  };

  var ImprovePage = function (cfg) {
    this.container = (cfg && cfg.container) || $('body');

    this.data = null;
    this.$body = null;
    this.$imgBox = null;

    this.renderLayout();
    this.bindEvents();
    this.fetchData();
  };

  ImprovePage.prototype = {

    fetchData : function (cfg) {
      var self = this,
          config = cfg || {},
          params = config.params || {},
          successFn = config.successFn || null;
      // show loading bar when fetching data
      self.$imgBox.html(self.genMaskHtml());
      MP.loadData('getImproveDetail', params/*, testData*/)
        .done(function (rsp) {
          self.data = rsp;
          successFn && successFn(rsp);
          self.render();
        })
        .fail(function (rsp) {
          alert('Fail to fetch data from server, please try again later.');
        })
        .always(function () {

        });
    },

    sendData : function (params) {
      var self = this;
      MP.loadData('setImproveDetail', params/*, testData*/)
        .done(function () {
          self.data = null;
          self.fetchData();
        })
        .fail(function (rsp) {
          alert('Fail to send data to server, please try again later.');
        });
    },

    mapLayoutRenderData : function () {
      return {
        title : 'Which One Is BETTER ?'
      };
    },

    renderLayout : function () {
      var self = this,
          renderData = self.mapLayoutRenderData(),
          htm = self.genLayoutHtml(renderData);
      self.container.html(htm);
      self.$body = self.container.find('.mp-ip-body');
      self.$imgBox = self.container.find('.ip-image-box');
    },

    mapRenderData : function () {
      var self = this, data = self.data;
      return {
        text : data.query,
        images : $.map(data.entrys, function (val, idx) {
          var v = JSON.parse(val);
          return {
            src : v.img || '',
            title : v.text || '',
            answer : idx == 0 ? 1 : -1
          };
        })
      };
    },

    render : function () {
      var self = this,
          renderData = self.mapRenderData(),
          htm = self.genImgBoxHtml(renderData);
      self.$imgBox.html(htm);
    },

    bindEvents : function () {
      var self = this;
      self.$imgBox
        .on('click', '.img-wrapper a', function () {
          var $this = $(this);
          self.sendData({
            itemID : self.data.itemID,
            answer : $this.data('answer')
          });
        })
        .on('click', '.btn.skip', function () {
          var $btn = $(this);
          $btn.button('loading');
          self.fetchData({
            successFn : function () {$btn.button('reset');}
          });
        })
        .on('imgerror', function (evt) {
          var ele = evt.target;
          ele.src = './imgs/default.png';
          ele.onerror = null;
        });
    },

    genLayoutHtml : function (ctx) {
      var self = this,
          title = ctx.title || '';
      return (
        '<header><h1>' + title + '</h1></header>' +
        '<div class="container mp-ip-body">' + 
          '<div class="alert alert-warning text-center">' + 
            'Thanks for helping us to improve the accuracy of matching. ' +
            'Please click on the image which you feel better fitting the text.' + 
          '</div>' + 
          '<div class="row well ip-image-box">' +
          '</div>' + 
        '</div>'
      );
    },

    genMaskHtml : function () {
      return (
        '<div class="mask-layer">' + 
          '<div class="progress">' +
            '<div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="45" aria-valuemin="0" aria-valuemax="100" style="width: 100%">' +
              '<span class="sr-only">L O A D I N G</span>' + 
            '</div>' +
          '</div>' +
        '</div>'
      );
    },

    genImgBoxHtml : function (ctx) {
      var self = this,
          text = ctx.text || '',
          images = ctx.images || [],
          imagesHtml = $.map(images, function (val) {
            return self.genImgHtml(val);
          }).join('');
      return (
        '<div class="col-xs-12 col-sm-8 col-sm-offset-2 text-center">' + 
          '<p class="query-text" >' + text + '</p>' +
        '</div>' + 
        '<div class="col-xs-12 col-sm-2">' +
          '<button class="btn btn-warning btn-sm pull-right skip" loadingText="loading...">SKIP</button>' +
        '</div>' +
        imagesHtml
      );
    },

    genImgHtml : function (ctx) {
      var clz = ctx.clz || 'col-xs-6',
          answer = ctx.answer || 1,
          src = ctx.src || '',
          title = ctx.title || 'PIC',
          alt = ctx.alt || title;
      return (
        '<div class="' + clz + '">' +
          '<div class="img-wrapper">' + 
            '<a href="javascript:void(0);" data-answer="' + answer + '">' + 
              '<img class="img-content" src="' + src + '" title="' + title + '" alt="' + alt + '" onerror="jQuery(this).trigger(\'imgerror\', (arguments[0]));" />' +
            '</a>' +
          '</div>' +
        '</div>'
      );
    }
  };

  MP.ImprovePage = ImprovePage;
  window.MP = MP;

})(jQuery, window, document);