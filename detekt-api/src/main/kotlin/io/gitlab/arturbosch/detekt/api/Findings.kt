package io.gitlab.arturbosch.detekt.api

import kotlin.reflect.full.memberProperties

/**
 * Base interface of detection findings. Inherits a bunch of useful behaviour
 * from sub interfaces.
 *
 * Basic behaviour of a finding is that is can be assigned to an id and a source code position described as
 * an entity. Metrics and entity references can also considered for deeper characterization.
 *
 * @author Artur Bosch
 */
interface Finding : Compactable, Reflective, HasEntity, HasMetrics {
	val id: String
	val issue: Issue
	val references: List<Entity>
}

/**
 * Describes a source code position.
 */
interface HasEntity {
	val entity: Entity
	val location: Location
		get() = entity.location
	val locationAsString: String
		get() = location.locationString
	val startPosition: SourceLocation
		get() = location.source
	val charPosition: TextLocation
		get() = location.text
	val file: String
		get() = location.file
	val signature: String
		get() = entity.signature
	val name: String
		get() = entity.name
	val inClass: String
		get() = entity.className
}

/**
 * Adds metric container behaviour.
 */
interface HasMetrics {
	val metrics: List<Metric>
	fun metricByType(type: String): Metric? = metrics.find { it.type == type }
}

/**
 * Allows to iterate over attributes reflectively.
 */
interface Reflective {
	fun findAttribute(name: String): Any? = this.javaClass.kotlin.memberProperties.find { it.name == name }?.get(this)
}

/**
 * Provides a compact string representation.
 */
interface Compactable {
	fun compact(): String
	fun compactWithSignature(): String = compact()
}
