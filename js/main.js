$(function() {
  // global settings
  var queryServer = 'http://114.215.99.92:8080/matchp-web/api/query?q=%E4%BB%8A%E5%A4%A9';
  var imgsRow     = 5;
  var imgsCol     = 4;

  // global variables
  var pageNum      = 0; // total page number
  var pageIndex    = 0; // current page index
  var totalData    = [];
  var $imgs        = [];
  var $preBtn      = $('#pre-btn');
  var $nxtBtn      = $('#nxt-btn');
  var $pageIndex   = $('#cur-page-index');
  var $pageNum     = $('#total-page-num');
  var $queryText   = $('#query-test');
  var $resultCount = $('#result-count');

  // Search button clicked
  $('#submit').on('click', function(e) {
    e.preventDefault();
    this.disabled = 'disabled'; // disable submit button while fetching data from server

    var that = this;
    var queryText = $queryText.val();
    var queryUrl = queryServer + 'query?q=' + queryText;

    $.ajax({
      url: queryUrl,
      dataType: 'json',
      type: 'get',
      success: function(data) {
        totalData = data;
        resultHandler();
        that.disabled = '';
      },
      error: function(xhr, status, err) {
        console.error(queryUrl, status, err.toString());
        that.disabled = '';
      }
    });
  });

  $preBtn.on('click', function() {
    showImgs(--pageIndex);
    $pageIndex.text(pageIndex + 1);
  });

  $nxtBtn.on('click', function() {
    showImgs(++pageIndex);
    $pageIndex.text(pageIndex + 1);
  });

  function resultHandler() {
    $resultCount.text(totalData.length);
    $pageNum.text(Math.ceil(totalData.length / imgsRow / imgsCol));

    if (!$imgs.length) {
      initImgsContainer('#imgs-container');
      $('#pager').removeClass('hidden');
    }

    showImgs(0);
  }

  function showImgs(page) {
    var index = page * imgsRow * imgsCol;

    for (var i = 0; i < imgsRow; i++) {
      for (var j = 0; j < imgsCol; j++) {
        if (totalData[index]) {
          $imgs[i][j].attr({
            src: totalData[index].url,
            alt: totalData[index].text,
            title: totalData[index].text
          });
          index++;
        }
        else {
          $imgs[i][j].replaceWith('');
        }
      }
    }

    $preBtn.attr({
      disabled: page ? null : 'disabled'
    });
    $nxtBtn.attr({
      disabled: (totalData.length > index) ? null : 'disabled'
    });
  }

  // Render images container
  function initImgsContainer(container) {
    var $imgsContainer = $(container);
    var $imgsFrag = $('<div></div>');

    for (var i = 0; i < imgsRow; i++) {
      $imgs[i] = [];
      for (var j = 0; j < imgsCol; j++) {
        $imgs[i][j] = $('<img></img>').on('error', function() { // in case of image not found
          $(this).replaceWith("<span>Image Not Found!</span>");
        });
        $imgsFrag.append($imgs[i][j]);
        $imgs[i][j].wrap('<div class="img-wrapper"></div>');
      }
      $imgsFrag.append($('<br class="clear" />'));
    }

    $imgsContainer.append($imgsFrag);
  }
});