export function timeFix () {
  const time = new Date()
  const hour = time.getHours()
  return hour < 9 ? '早上好' : (hour <= 11 ? '上午好' : (hour <= 13 ? '中午好' : (hour < 20 ? '下午好' : '晚上好')))
}

export function welcome () {
  const arr = ['休息一会儿吧', '准备吃什么呢?', '要不要打一把 DOTA', '我猜你可能累了']
  const index = Math.floor((Math.random) * arr.length)
  return arr[index]
}

export const getObjType = obj => {
  var toString = Object.prototype.toString
  var map = {
    '[object Boolean]': 'boolean',
    '[object Number]': 'number',
    '[object String]': 'string',
    '[object Function]': 'function',
    '[object Array]': 'array',
    '[object Date]': 'date',
    '[object RegExp]': 'regExp',
    '[object Undefined]': 'undefined',
    '[object Null]': 'null',
    '[object Object]': 'object'
  }
  if (obj instanceof Element) {
    return 'element'
  }
  return map[toString.call(obj)]
}

/**
 * 对象深拷贝
 */
export const deepClone = data => {
  var type = getObjType(data)
  var obj
  if (type === 'array') {
    obj = []
  } else if (type === 'object') {
    obj = {}
  } else {
    // 不再具有下一层次
    return data
  }
  if (type === 'array') {
    for (var i = 0, len = data.length; i < len; i++) {
      obj.push(deepClone(data[i]))
    }
  } else if (type === 'object') {
    for (var key in data) {
      obj[key] = deepClone(data[key])
    }
  }
  return obj
}

/**
 * 触发 window.resize
 */
export function triggerWindowResizeEvent () {
  const event = document.createEvent('HTMLEvents')
  event.initEvent('resize', true, true)
  event.eventType = 'message'
  window.dispatchEvent(event)
}

