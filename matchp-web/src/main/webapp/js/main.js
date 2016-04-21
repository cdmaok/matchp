$(function() {
  // global settings
  var options = {
    queryServer: 'http://114.215.99.92:8080/matchp-web/api/',
    imgsRow: 5,
    imgsCol: 4
  }

  // global variables
  var gPageNum      = 0; // total page number
  var gTotalData    = []; // the total data from server

  // jquery DOM objects
  var $imgsPages     = [];
  var $result        = $('#result-container');
  var $imgsContainer = $('#imgs-container');
  var $pager         = $('#pager');
  var $preBtn        = $('#pre-btn');
  var $nxtBtn        = $('#nxt-btn');
  var $pageIndex     = $('#cur-page-index');
  var $pageNum       = $('#total-page-num');
  var $queryText     = $('#query-text');
  var $resultCount   = $('#result-count');

  $.support.cors = true;

  // bind UI element events
  $('#submit').on('click', function(e) {
    e.preventDefault();
    this.disabled = 'disabled'; // disable submit button while fetching data from server

    var that = this;
    var queryText = $queryText.val();
    var queryUrl = options.queryServer + 'query?q=' + queryText;

    $.ajax({
      url: queryUrl,
      dataType: 'json',
      type: 'get',
      success: function(data) {
        resultHandler(data);
        that.disabled = '';
      },
      error: function(xhr, status, err) {
        console.error(queryUrl, status, err.toString());
        alert('Failed to fetch data from server...Orz');
        that.disabled = '';
      }
    });
  });

  $preBtn.on('click', function() {
    var curIndex = +$pageIndex.text() - 1;
    $pageIndex.text(--curIndex + 1);
    showImgsPage(curIndex, -1);
  });

  $nxtBtn.on('click', function() {
    var curIndex = +$pageIndex.text() - 1;
    $pageIndex.text(++curIndex + 1);
    showImgsPage(curIndex, 1);
  });

  // ajax success resultHandler
  function resultHandler(data) {

    // init data for rendering
    gTotalData = data;
    $imgsPages = [];
    $imgsContainer.html('');

    $result.removeClass('hidden');
    $resultCount.text(gTotalData.length);

    gPageNum = (Math.ceil(gTotalData.length / options.imgsRow / options.imgsCol));
    if (gPageNum > 1) {
      $pager.removeClass('hidden');
      $pageNum.text((Math.ceil(gTotalData.length / options.imgsRow / options.imgsCol)));
    }

    showImgsPage(0, 0);
  }

  function showImgsPage(pageIndex, delta) {
    var $curPage = $imgsPages[pageIndex] || renderPage(pageIndex);

    if (delta > 0) {  // next page
      $imgsPages[pageIndex - 1].hide();
    } else if (delta < 0) { // previous page
      $imgsPages[pageIndex + 1].hide();
    }
    $curPage.show();

    if (!$pager.hasClass('hidden')) {
      $preBtn.attr({
        disabled: pageIndex ? null : 'disabled'
      });
      $nxtBtn.attr({
        disabled: (pageIndex === gPageNum - 1) ? 'disabled' : null
      });
    }
  }

  // Render a page of images
  function renderPage(pageIndex) {
    var $page = $('<div></div>').attr({
      'id': 'imgs-page-' + pageIndex, 
      'class': 'container-fluid imgs-page'
    });
    var index = pageIndex * options.imgsRow * options.imgsCol;
    var imgWrapperSize = Math.floor(80 / options.imgsCol) + '%';
    var imgWrapperMargin = Math.floor(20 / options.imgsCol / 2) + '%';

    for (var i = 0; i < options.imgsRow && gTotalData[index]; i++) {
      var $row = $('<div></div>').addClass('row');
      for (var j = 0; j < options.imgsCol && gTotalData[index]; j++) {
        var $imgWrapper = $('<div></div>').attr({
          class: 'img-wrapper',
          style: ['width:' + imgWrapperSize, 
                  'padding-top:' + imgWrapperSize, 
                  'margin:' + imgWrapperMargin].join(';')
        });
        var $img = $('<img />').attr({
          src: gTotalData[index].url, 
          title: gTotalData[index].text, 
          alt: gTotalData[index].text
          })
          .addClass('img-content')
          .on('error', function() { // in case of image not found
            this.src = './imgs/default.png';
            this.onerror = null; // in case of error loop
          });
        $row.append($imgWrapper.append($img));
        index++;
      }
      $page.append($row);
    }

    $imgsPages[pageIndex] = $page;
    $imgsContainer.append($page);
    return $page;
  }
});
