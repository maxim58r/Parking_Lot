package parking

import car.Car
import kotlin.system.exitProcess

class Parking {
    private val leaveCommand = "leave"
    private val parkCommand = "park"
    private val exitCommand = "exit"
    private val status = "status"
    private val create = "create"
    private val regByColor = "reg_by_color"
    private val spotByColor = "spot_by_color"
    private val spotByReg = "spot_by_reg"
    private var case: MutableList<String> = mutableListOf()
    private var parkSize: Int = 0
    private var firstFreeSpot = 0
    private var freeSpot = 0
    private var parking = mutableMapOf<Int, Car>()

    fun start() {
        while (true) {
            getCommand()
        }
    }

    private fun getCommand() {
        case = readln().split(" ").toMutableList()
        val first = case.first()

        if (first == exitCommand) {
            exitProcess(1)
        }

        if (checkParkingSpot(first)) {
            firstFreeSpot = parking.entries.firstOrNull { k -> k.value.number.isBlank() }?.key ?: 0
            freeSpot = parking.entries.count { park -> park.value.number.isBlank() }

            when (first) {
                parkCommand -> park()
                leaveCommand -> leave(case[1].toInt())
                status -> getStatus()
                create -> cretePark(case)
                regByColor -> getRegByColor(case[1])
                spotByReg -> getSpotByReg(case[1])
                spotByColor -> getSpotByColor(case[1])
            }
        }
    }

    private fun getSpotByReg(regNumber: String) {
        val regNums = parking.entries
            .filter { entity -> entity.value.number == regNumber }
            .map { car -> car.key.toString() }
            .toList()
        if (regNums.isNotEmpty()) {
            println(regNums.joinToString ( ", " ))
        } else println("No cars with registration number $regNumber were found.")
    }

    private fun getRegByColor(color: String) {
        val spots = parking.entries
            .filter { entity -> entity.value.color.lowercase() == color.lowercase() }
            .map { car -> car.value.number }
        if (spots.isNotEmpty()) {
            println(spots.joinToString ( ", " ))
        } else println("No cars with color $color were found.")
    }

    private fun getSpotByColor(color: String) {
        val spots = parking.entries
            .filter { entity -> entity.value.color.lowercase() == color.lowercase() }
            .map { car -> car.key }
            .toList()
        if (spots.isNotEmpty()) {
            println(spots.joinToString ( ", " ))
        } else println("No cars with color $color were found.")
    }

    private fun cretePark(sizePark: MutableList<String>) {
        var countParkSpots = 0
        parkSize = sizePark[1].toInt()
        parking.clear()
        repeat(parkSize) { parking[++countParkSpots] = Car("", "") }
        println("Created a parking lot with $parkSize spots.")
    }

    private fun park() {
        val car = Car(case[1], case[2])

        if (firstFreeSpot != 0) {
            parking[firstFreeSpot] = car
            val colorCar = parking[firstFreeSpot]?.color
            println("$colorCar car parked in spot $firstFreeSpot.")
        } else {
            println("Sorry, the parking lot is full.")
        }
    }

    private fun leave(spot: Int) {
        parking.replace(spot, Car("", ""))
        println("Spot $spot is free.")
    }

    private fun getStatus() {
        if (freeSpot == parkSize) {
            println("Parking lot is empty.")
        } else {
            parking.entries
                .filter { park -> park.value.number.isNotEmpty() }
                .forEach { park -> println("${park.key} ${park.value.number} ${park.value.color}") }
        }
    }

    private fun checkParkingSpot(command: String): Boolean {
        return if (parkSize <= 0 && command != create) {
            println("Sorry, a parking lot has not been created.")
            false
        } else true
    }
}