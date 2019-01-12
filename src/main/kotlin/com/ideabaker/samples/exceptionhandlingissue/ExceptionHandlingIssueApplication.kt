package com.ideabaker.samples.exceptionhandlingissue

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import kotlin.IllegalArgumentException

@SpringBootApplication
class ExceptionHandlingIssueApplication

fun main(args: Array<String>) {
	runApplication<ExceptionHandlingIssueApplication>(*args)
}


@RestController
class HomeController {
	@GetMapping("/success")
	fun success(): Mono<String> = Mono.just("Hello")

	@GetMapping("/mono-error-string")
	fun monoErrorString(): Mono<String> = Mono.error(IllegalArgumentException())

	@GetMapping("/mono-error-string-map")
	fun monoErrorStringMap(): Mono<String> = Mono.error(IllegalStateException())

	@GetMapping("/mono-error-long")
	fun monoErrorLong(): Mono<Long> = Mono.error(IllegalStateException())

	@GetMapping("/mono-error-bool")
	fun monoErrorBool(): Mono<Boolean> = Mono.error(IllegalStateException())

	@GetMapping("/mono-error-int")
	fun monoErrorInt(): Mono<Int> = Mono.error(IllegalStateException())

	@GetMapping("/throw")
	fun monoError3(): Mono<String> {
		throw IllegalStateException()
	}
}

@RestControllerAdvice
class ErrorHandler {
	@ExceptionHandler(IllegalStateException::class)
	@ResponseBody
	@ResponseStatus(HttpStatus.CONFLICT)
	internal fun handleIllegalStateException(ex: IllegalStateException, exchange: ServerWebExchange): Mono<Map<String, String>> {
        val responseBody = HashMap<String, String>()
        responseBody["message"] = "Error Happened"
        responseBody["req"] = exchange.request.uri.toASCIIString()
		return Mono.just(responseBody)
	}

	@ExceptionHandler(IllegalArgumentException::class)
	@ResponseBody
	@ResponseStatus(HttpStatus.CONFLICT)
	internal fun handleIllegalArgumentException(ex: IllegalArgumentException, exchange: ServerWebExchange): Mono<String> {
		return Mono.just("Error Happened")
	}
}

