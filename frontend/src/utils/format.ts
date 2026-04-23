/**
 * 格式化日期时间，去除'T'字符
 * @param dateStr 日期字符串
 * @returns 格式化后的日期字符串
 */
export const formatDateTime = (dateStr: string | null | undefined): string => {
  if (!dateStr) return ''
  // 替换 T 为 空格
  return dateStr.replace('T', ' ')
}

/**
 * 格式化日期时间为 YYYY-MM-DD HH:mm:ss 格式
 * @param dateStr 日期字符串或Date对象
 * @returns 格式化后的日期字符串
 */
export const formatDateTimeFull = (dateStr: string | Date | null | undefined): string => {
  if (!dateStr) return ''
  const date = typeof dateStr === 'string' ? new Date(dateStr) : dateStr
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}
