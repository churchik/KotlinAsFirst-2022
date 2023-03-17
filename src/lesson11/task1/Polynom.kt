@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import java.util.NoSuchElementException
import kotlin.math.pow

/**
 * Класс "полином с вещественными коэффициентами".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса -- полином от одной переменной (x) вида 7x^4+3x^3-6x^2+x-8.
 * Количество слагаемых неограничено.
 *
 * Полиномы можно складывать -- (x^2+3x+2) + (x^3-2x^2-x+4) = x^3-x^2+2x+6,
 * вычитать -- (x^3-2x^2-x+4) - (x^2+3x+2) = x^3-3x^2-4x+2,
 * умножать -- (x^2+3x+2) * (x^3-2x^2-x+4) = x^5+x^4-5x^3-3x^2+10x+8,
 * делить с остатком -- (x^3-2x^2-x+4) / (x^2+3x+2) = x-5, остаток 12x+16
 * вычислять значение при заданном x: при x=5 (x^2+3x+2) = 42.
 *
 * В конструктор полинома передаются его коэффициенты, начиная со старшего.
 * Нули в середине и в конце пропускаться не должны, например: x^3+2x+1 --> Polynom(1.0, 2.0, 0.0, 1.0)
 * Старшие коэффициенты, равные нулю, игнорировать, например Polynom(0.0, 0.0, 5.0, 3.0) соответствует 5x+3
 */
class Polynom(private vararg val coeffs: Double) {
    private val nc = coeffs.reversed().takeWhile { it != 0.0 }.toDoubleArray()

    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double = nc.getOrNull(i) ?: throw NoSuchElementException()

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double {
        var value = 0.0
        for (index in nc.indices) value += nc[index] * x.pow(index)
        return value
    }

    /**
     * Степень (максимальная степень x при ненулевом слагаемом, например 2 для x^2+x+1).
     *
     * Степень полинома с нулевыми коэффициентами считать равной 0.
     * Слагаемые с нулевыми коэффициентами игнорировать, т.е.
     * степень 0x^2+0x+2 также равна 0.
     */
    fun degree(): Int = if (nc.isNotEmpty()) nc.size - 1 else 0

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        val nc2 = nc.toMutableList()
        for (order in other.nc.indices) if (order < nc2.size) nc2[order] += other.nc[order]
        else nc2.add(other.nc[order])
        return Polynom(*nc2.reversed().toDoubleArray())
    }


    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom = Polynom(*nc.map { it * -1 }.reversed().toDoubleArray())


    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom = this + (-other)

    /**
     * Умножение
     */
    operator fun times(other: Polynom): Polynom {
        val map = mutableMapOf<Int, Double>()
        for (now in nc.indices) {
            for (next in other.nc.indices) {
                if (map.keys.contains(now + next)) map[now + next] =
                    nc[now] * other.nc[next] + map[now + next]!!
                else map[now + next] = nc[now] * other.nc[next]
            }
        }
        return Polynom(*map.values.reversed().toDoubleArray())
    }

    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */
    operator fun div(other: Polynom): Polynom = TODO()

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom = TODO()

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is Polynom && other.nc.contentEquals(nc)

    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int = nc.contentHashCode()
}
