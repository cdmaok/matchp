$(function() {
  var queryServer = 'http://114.215.99.92:8080/matchp-web/api/query?q=%E4%BB%8A%E5%A4%A9';
  var imgsRow = 5;
  var imgsCol = 2;
  var $imgs = [];

  $('#submit').on('click', function(e) {
    e.preventDefault();
    var queryText = $('#query-text').val();
    var queryUrl = queryServer + 'query?q=' + queryText;

    $.ajax({
      url: queryUrl,
      dataType: 'json',
      type: 'get',
      success: function(data) {
        resultHandler(data);
      },
      error: function(xhr, status, err) {
        console.error(queryUrl, status, err.toString());
      }
    });
  });

  function resultHandler(data) {
    $('#result-count').text(data.length);

    if (!$imgs.length) initImgsContainer('#imgs-container');

    showImgs(data, 0);
  }

  function showImgs(data, page) {
    var index = page * imgsRow * imgsCol;

    for (var i = 0; i < imgsRow; i++) {
      for (var j = 0; j < imgsCol; j++) {
        if (data[index]) {
          $imgs[i][j].attr({
            src: data[index].url,
            alt: data[index].text,
            title: data[index].text
          });
          index++;
        }
        else {
          return;
        }
      }
    }
  }

  function initImgsContainer(container) {
    var $imgsContainer = $(container);
    var $imgsFrag = $('<div></div>');

    for (var i = 0; i < imgsRow; i++) {
      $imgs[i] = [];
      for (var j = 0; j < imgsCol; j++) {
        $imgs[i][j] = $('<img></img>');
        $imgsFrag.append($imgs[i][j]);
        $imgs[i][j].wrap('<span class="img-wrapper"></span>');
      }
      $imgsFrag.append($('<br />'));
    }

    $imgsContainer.append($imgsFrag);
  }
});