package org.hackinghealth.cheesecloth.algo

/**
 * Created by mabushawish on 6/5/17.
 */
import butterknife.internal.Utils.listOf
import org.hackinghealth.cheesecloth.dao.*
import java.sql.DriverManager.println
import java.util.*

data class Location(val lat: Double = 0.0, val long: Double = 0.0)
data class Context(val location: Location = Location(), val currentTime: Date = Date())

typealias AnalysisNode = (Message, Array<Category>, () -> Unit) -> Unit

class Classifier {
    // Categories that the messages will be weighted for
    var categories: Array<Category> = arrayOf()

    // Weights for priority
    var priorityContentWeights: Array<Tag> = arrayOf()
    var prioritySenderWeights: Array<Tag> = arrayOf()

    /**
     *  Optional callback hook to a custom analysis node.
     */
    public var analysisRootNode: AnalysisNode? = null

    /**
     *  Initialize the classifier to begin processing messages into categories and weights.
     */
    fun initialize(categories: Array<Category> = arrayOf(),
                   priorityContentWeights: Array<Tag> = arrayOf(),
                   prioritySenderWeights: Array<Tag> = arrayOf()) {
        this.categories = categories
        this.priorityContentWeights = priorityContentWeights
        this.prioritySenderWeights = prioritySenderWeights
    }

    /**
     *  Processes a message and calls a callback when weighting and categorization are done.
     */
    fun processMessage(message: Message, context: Context, callback: (Message, Array<CategoryAssociation>, Double?) -> Unit) {
        var categories = categoriesForMessage(message)
        var weight = weightForMessage(message)

        if (analysisRootNode != null) {
            analysisRootNode?.invoke(message, this.categories, { callback(message, categories, weight) })
        } else {
            callback(message, categories, weight)
        }
    }

    /**
     *  Generates weighted category associations for the message with all available categories.
     */
    fun categoriesForMessage(message: Message): Array<CategoryAssociation> {
        // TODO: Magical binning algorithm!
        var categoryAssociations: MutableList<CategoryAssociation> = mutableListOf()
        for (category in categories) {
            var accumulator: Double = 0.0
            var quantity: Int = 0
            for (tag in category.tags) {
                var index: Int = 0
                do {
                    var result = message.text.findAnyOf(listOf(tag.text), index)
                    if (result != null) {
                        index = result?.first + tag.text.length
                        accumulator += tag.weight
                        quantity += 1
                    }
                } while (result != null)
            }

            for (tag in category.senders) { // TODO: Functional form
                if (tag.text == message.sender.address) {
                    accumulator += tag.weight
                    quantity += 1
                }
            }

            val avg: Double = if (quantity > 0) accumulator / quantity else 0.0
            categoryAssociations.add(CategoryAssociation(category, avg))
        }
        return categoryAssociations.toTypedArray()
    }

    fun weightForMessage(message: Message): Double {
        // TODO: Magical magnitude algorithm!
        var accumulator: Double = 0.0
        var quantity: Int = 0
        for (tag in priorityContentWeights) {
            var index: Int = 0
            do {
                var result = message.text.findAnyOf(listOf(tag.text), index)
                if (result != null) {
                    index = result?.first + tag.text.length
                    accumulator += tag.weight
                    quantity += 1
                }
            } while (result != null)
        }

        for (tag in prioritySenderWeights) { // TODO: Functional form
            if (tag.text == message.sender.address) {
                accumulator += tag.weight
                quantity += 1
            }
        }
        return if (quantity > 0) accumulator / quantity else 0.0
    }
}

/**
 *  Test driver.
 */
fun populate() {

////////////////////
// Dummy Messages //
////////////////////

    val john = Sender("John Doe", "john.doe@email.com")
    val laura = Sender("Laura Smith", "laura.smith@email.com")
    val mary = Sender("Mary York", "mary.york@work.com")
    val liu = Sender("Liu Wang", "wang.liu@work.com")
    val HRDept = Sender("HR Dept.", "hr@work.com")
    val dummyWorkMessages = arrayListOf(
            Message(mary, "You have a bloodtest to pick up in lab B."),
            Message(liu, "Remember to donate to the blood drive this Friday."),
            Message(mary, "There are two pending x-rays in your inbox. Please ensure all x-rays are processed by EOD."),
            Message(mary, "Hey, didf you gte all the xrays?"),
            Message(HRDept, "Congratulations to Mr. James on his recent promotion!")
    )

    val dummyPersonalMessages = arrayListOf(
            Message(john, "Hi, what's for dinner today sweetheart?"),
            Message(laura, "Hi! We had a great time at dinner last night, lets do it again soon!!!"),
            Message(john, "Ok, I picked us up some takeout from the new Korean place.")
    )

    val workTags = arrayListOf(
            Tag("blood", 0.75),
            Tag("lab", 0.95),
            Tag("x-ray", 1.0),
            Tag("xray", 1.0)
    )

    val workSenderTags = arrayListOf(
            Tag(john.address, -1.0),
            Tag(laura.address, -1.0),
            Tag(mary.address, 1.0),
            Tag(liu.address, 1.0),
            Tag(HRDept.address, 0.5)
    )

    val personalTags = arrayListOf(
            Tag("dinner", 1.0),
            Tag("sweetheart", 1.0),
            Tag("x-ray", -1.0),
            Tag("xray", -1.0)
    )

    val personalSenderTags = arrayListOf(
            Tag(john.address, 1.0),
            Tag(laura.address, 1.0),
            Tag(mary.address, -1.0),
            Tag(liu.address, -1.0),
            Tag(HRDept.address, 0.5)
    )

    val priorityContentTags = arrayOf(
            Tag("blood", 0.75),
            Tag("lab", 0.5),
            Tag("x-ray", 1.0),
            Tag("xray", 1.0),
            Tag("dinner", -0.5)
    )

    val prioritySenderTags = arrayOf(
            Tag(john.address, 1.0),
            Tag(laura.address, 0.25),
            Tag(mary.address, 1.0),
            Tag(liu.address, 0.5),
            Tag(HRDept.address, -1.0)
    )

///////////////////////////
// Initialize Classifier //
///////////////////////////
    val workCategory = Category("Work", workTags, workSenderTags)
    val personalCategory = Category("Personal", personalTags, personalSenderTags)
    val classifier = Classifier()
    classifier.initialize(arrayOf(workCategory, personalCategory), priorityContentTags, prioritySenderTags)

////////////////////////////
// Classify Test Messages //
////////////////////////////
    var priorityMap: MutableMap<Category, Double> = mutableMapOf(workCategory to 0.0, personalCategory to 0.0)

    for (message in dummyWorkMessages.plus(dummyPersonalMessages)) {

        CheeseClothDatabaseHelper.writeMessageToDB(message)

        classifier.processMessage(message, Context(), { message, categories, weight ->

            if ((weight ?: 0.0) > 0.0) {
                for (categoryAssoc in categories) {
                    if (categoryAssoc.weight > 0.0) {
                        val magnitude: Double = (weight ?: 0.0) * categoryAssoc.weight
                        priorityMap[categoryAssoc.category] = (priorityMap[categoryAssoc.category] ?: 0.0) + magnitude
                    }
                }
            }

            println("========== Message classified! ==========")
            println("${message.text}")
            println("Overall priority: $weight")
            val sorted = categories.sortedArrayWith(compareBy<CategoryAssociation> { it.weight }).reversed()
            for (cat in sorted) {
                CheeseClothDatabaseHelper.writeCategoryAssociationToDb(cat)
                println("${cat.category.name} : ${cat.weight}")
            }
        })
    }

    println("========================")
    println("Category priority level:")
    for ((k, v) in priorityMap) {
        println("${k.name} = $v")
    }
}