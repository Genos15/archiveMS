package com.thintwice.archive.mbompay.domain.common


object IMEIValidator {
    fun isValidIMEI(imei: String?): Boolean {
        if (imei == null) return false
        var n: Long = imei.toLongOrNull() ?: 0L
        val l: Int = imei.length
        return if (l != 15) // If length is not 15 then IMEI is Invalid
            false else {
            var d: Int
            var sum = 0
            for (i in 15 downTo 1) {
                d = (n % 10).toInt()
                if (i % 2 == 0) {
                    d *= 2 // Doubling every alternate digit
                }
                sum += sumDig(d) // Finding sum of the digits
                n /= 10
            }/*sum % 10 == 0 && */
            sum != 0
        }
    }

    private fun sumDig(n: Int): Int {
        var n2 = n
        var a = 0
        while (n2 > 0) {
            a += n % 10
            n2 /= 10
        }
        return a
    }
}