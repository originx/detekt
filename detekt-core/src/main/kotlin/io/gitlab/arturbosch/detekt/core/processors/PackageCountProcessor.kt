package io.gitlab.arturbosch.detekt.core.processors

import io.gitlab.arturbosch.detekt.api.DetektVisitor
import io.gitlab.arturbosch.detekt.api.Detektion
import io.gitlab.arturbosch.detekt.api.FileProcessListener
import io.gitlab.arturbosch.detekt.api.ProjectMetric
import org.jetbrains.kotlin.com.intellij.openapi.util.Key
import org.jetbrains.kotlin.psi.KtFile

class PackageCountProcessor : FileProcessListener {

	private val visitor = PackageCountVisitor()
	private val key = NUMBER_OF_PACKAGES_KEY

	override fun onProcess(file: KtFile) {
		file.accept(visitor)
	}

	override fun onFinish(files: List<KtFile>, result: Detektion) {
		val count = files
				.mapNotNull { it.getUserData(key) }
				.distinct()
				.size
		result.add(ProjectMetric(key.toString(), count))
	}
}

val NUMBER_OF_PACKAGES_KEY = Key<String>("number of packages")

class PackageCountVisitor : DetektVisitor() {

	override fun visitKtFile(file: KtFile) {
		val packageName = file.packageFqNameByTree.toString()
		file.putUserData(NUMBER_OF_PACKAGES_KEY, packageName)
	}
}
