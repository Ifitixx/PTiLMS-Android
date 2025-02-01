package com.pizzy.ptilms.data

import androidx.room.withTransaction
import com.pizzy.ptilms.AppDatabase
import com.pizzy.ptilms.data.DatabaseConstants.COURSE_CODE
import com.pizzy.ptilms.data.DatabaseConstants.COURSE_DESCRIPTION
import com.pizzy.ptilms.data.DatabaseConstants.COURSE_FORMAT
import com.pizzy.ptilms.data.DatabaseConstants.COURSE_IMAGE_PATH
import com.pizzy.ptilms.data.DatabaseConstants.COURSE_PDF_PATH
import com.pizzy.ptilms.data.DatabaseConstants.COURSE_TITLE
import com.pizzy.ptilms.data.DatabaseConstants.COURSE_VIDEO_PATH
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_COMPUTER_ENGINEERING_TECHNOLOGY
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_COMPUTER_SCIENCE_IT
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_ELECTRICAL_ELECTRONICS_ENG_TECH
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_GENERAL
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_INDUSTRIAL_SAFETY_ENV_TECH
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_MECHANICAL_ENGINEERING_TECHNOLOGY
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_PETROLEUM_ENGINEERING_TECHNOLOGY
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_PETROLEUM_MARKETING_BUSINESS_STUDIES
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_PETROLEUM_NATURAL_GAS_PROCESSING_TECH
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_SCIENCE_LABORATORY_TECHNOLOGY
import com.pizzy.ptilms.data.DatabaseConstants.DEPARTMENT_WELDING_ENGINEERING_OFFSHORE_TECH
import com.pizzy.ptilms.data.DatabaseConstants.LEVEL_HND1
import com.pizzy.ptilms.data.DatabaseConstants.LEVEL_HND2
import com.pizzy.ptilms.data.DatabaseConstants.LEVEL_ND1
import com.pizzy.ptilms.data.DatabaseConstants.LEVEL_ND2
import com.pizzy.ptilms.data.local.CourseDao
import com.pizzy.ptilms.data.local.DepartmentDao
import com.pizzy.ptilms.data.local.DepartmentLevelDao
import com.pizzy.ptilms.data.local.LevelDao
import com.pizzy.ptilms.data.model.DepartmentEntity
import com.pizzy.ptilms.data.model.DepartmentLevelCrossRef
import com.pizzy.ptilms.data.model.LevelEntity
import com.pizzy.ptilms.utils.createCourseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

class DatabasePopulator(
    private val departmentDao: DepartmentDao,
    private val levelDao: LevelDao,
    private val courseDao: CourseDao,
    private val departmentLevelDao: DepartmentLevelDao,
    private val appDatabase: AppDatabase
) {

    private val departments = listOf(
        DepartmentEntity(name = DEPARTMENT_COMPUTER_ENGINEERING_TECHNOLOGY),
        DepartmentEntity(name = DEPARTMENT_COMPUTER_SCIENCE_IT),
        DepartmentEntity(name = DEPARTMENT_ELECTRICAL_ELECTRONICS_ENG_TECH),
        DepartmentEntity(name = DEPARTMENT_INDUSTRIAL_SAFETY_ENV_TECH),
        DepartmentEntity(name = DEPARTMENT_MECHANICAL_ENGINEERING_TECHNOLOGY),
        DepartmentEntity(name = DEPARTMENT_PETROLEUM_ENGINEERING_TECHNOLOGY),
        DepartmentEntity(name = DEPARTMENT_PETROLEUM_MARKETING_BUSINESS_STUDIES),
        DepartmentEntity(name = DEPARTMENT_PETROLEUM_NATURAL_GAS_PROCESSING_TECH),
        DepartmentEntity(name = DEPARTMENT_SCIENCE_LABORATORY_TECHNOLOGY),
        DepartmentEntity(name = DEPARTMENT_WELDING_ENGINEERING_OFFSHORE_TECH),
        DepartmentEntity(name = DEPARTMENT_GENERAL)
    )

    private val levels = listOf(
        LevelEntity(name = LEVEL_ND1),
        LevelEntity(name = LEVEL_ND2),
        LevelEntity(name = LEVEL_HND1),
        LevelEntity(name = LEVEL_HND2),
    )

    private val courseDefinitions = listOf(
        Triple(DEPARTMENT_COMPUTER_SCIENCE_IT, LEVEL_ND1, listOf(
            mapOf(
                COURSE_TITLE to "Introduction to Programming",
                COURSE_CODE to "COM 111",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Introduction to Programming",
                COURSE_PDF_PATH to "path/to/pdf1.pdf",
                COURSE_IMAGE_PATH to "path/to/image1.jpg",
                COURSE_VIDEO_PATH to "path/to/video1.mp4"
            )
        )),
        Triple(DEPARTMENT_ELECTRICAL_ELECTRONICS_ENG_TECH, LEVEL_ND1, listOf(
            mapOf(
                COURSE_TITLE to "Circuit Analysis",
                COURSE_CODE to "EEE 111",
                COURSE_FORMAT to "PDF",
                COURSE_DESCRIPTION to "Circuit Analysis",
                COURSE_PDF_PATH to "path/to/pdf2.pdf",
                COURSE_IMAGE_PATH to "path/to/image2.jpg",
                COURSE_VIDEO_PATH to "path/to/video2.mp4"
            )
        )),
        Triple(DEPARTMENT_GENERAL, LEVEL_ND1, listOf(
            mapOf(
                COURSE_TITLE to "Introduction to Statistics",
                COURSE_CODE to "MTH 111",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Introduction to Statistics"
            ),
            mapOf(
                COURSE_TITLE to "Introduction to Communication in English",
                COURSE_CODE to "GNS 111",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Introduction to Communication in English"
            ),
            mapOf(
                COURSE_TITLE to "Introduction to Petroleum Engineering",
                COURSE_CODE to "PET 101",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "Introduction to Petroleum Engineering"
            )
        )),
        Triple(DEPARTMENT_GENERAL, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Communication Skills",
                COURSE_CODE to "GNS 201",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Communication Skills"
            ),
            mapOf(
                COURSE_TITLE to "Engineering Mathematics II",
                COURSE_CODE to "MTH 201",
                COURSE_FORMAT to "PDF",
                COURSE_DESCRIPTION to "Engineering Mathematics II"
            )
        )),
        Triple(DEPARTMENT_COMPUTER_ENGINEERING_TECHNOLOGY, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Digital Electronics",
                COURSE_CODE to "CET 222",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "Digital Electronics"
            )
        )),
        Triple(DEPARTMENT_COMPUTER_SCIENCE_IT, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Data Structures and Algorithms",
                COURSE_CODE to "COM 222",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Data Structures and Algorithms"
            ),
            mapOf(
                COURSE_TITLE to "Object-Oriented Programming",
                COURSE_CODE to "COM 212",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Object-Oriented Programming"
            )
        )),
        Triple(DEPARTMENT_MECHANICAL_ENGINEERING_TECHNOLOGY, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Thermodynamics",
                COURSE_CODE to "MEC 211",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Thermodynamics"
            )
        )),
        Triple(DEPARTMENT_ELECTRICAL_ELECTRONICS_ENG_TECH, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Power Electronics",
                COURSE_CODE to "EEE 201",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "Power Electronics"
            )
        )),
        Triple(DEPARTMENT_COMPUTER_SCIENCE_IT, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Database Management Systems",
                COURSE_CODE to "COM 414",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Database Management Systems"
            ),
            mapOf(
                COURSE_TITLE to "Artificial Intelligence",
                COURSE_CODE to "COM 411",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Artificial Intelligence"
            )
        )),
        Triple(DEPARTMENT_ELECTRICAL_ELECTRONICS_ENG_TECH, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Control Systems",
                COURSE_CODE to "EEE 301",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "Control Systems"
            )
        )),
        Triple(DEPARTMENT_INDUSTRIAL_SAFETY_ENV_TECH, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Environmental Impact Assessment",
                COURSE_CODE to "IET 201",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Environmental Impact Assessment"
            )
        )),
        Triple(DEPARTMENT_INDUSTRIAL_SAFETY_ENV_TECH, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Safety Engineering",
                COURSE_CODE to "IET 301",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Safety Engineering"
            )
        )),
        Triple(DEPARTMENT_GENERAL, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Entrepreneurship",
                COURSE_CODE to "GNS 401",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Entrepreneurship"
            ),
            mapOf(
                COURSE_TITLE to "Project Management",
                COURSE_CODE to "GNS 402",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Project Management"
            )
        )),
        Triple(DEPARTMENT_COMPUTER_SCIENCE_IT, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Software Engineering",
                COURSE_CODE to "COM 415",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Software Engineering"
            )
        )),
        Triple(DEPARTMENT_MECHANICAL_ENGINEERING_TECHNOLOGY, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Machine Design",
                COURSE_CODE to "MEC 412",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Machine Design"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_ENGINEERING_TECHNOLOGY, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Petroleum Geology",
                COURSE_CODE to "PET 201",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Petroleum Geology"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_ENGINEERING_TECHNOLOGY, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Well Logging",
                COURSE_CODE to "PET 301",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Well Logging"
            )
        )),
        Triple(DEPARTMENT_COMPUTER_SCIENCE_IT, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Expert Systems",
                COURSE_CODE to "COM 422",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Expert Systems"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_MARKETING_BUSINESS_STUDIES, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Marketing Principles",
                COURSE_CODE to "PMB 201",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Marketing Principles"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_MARKETING_BUSINESS_STUDIES, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Financial Management",
                COURSE_CODE to "PMB 301",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Financial Management"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_NATURAL_GAS_PROCESSING_TECH, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Natural Gas Processing",
                COURSE_CODE to "PNG 201",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Natural Gas Processing"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_NATURAL_GAS_PROCESSING_TECH, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Pipeline Engineering",
                COURSE_CODE to "PNG 301",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Pipeline Engineering"
            )
        )),
        Triple(DEPARTMENT_COMPUTER_SCIENCE_IT, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Computer Graphics",
                COURSE_CODE to "COM 426",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Computer Graphics"
            )
        )),
        Triple(DEPARTMENT_MECHANICAL_ENGINEERING_TECHNOLOGY, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Fluid Mechanics",
                COURSE_CODE to "MEC 201",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Fluid Mechanics"
            )
        )),
        Triple(DEPARTMENT_MECHANICAL_ENGINEERING_TECHNOLOGY, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Machine Tools",
                COURSE_CODE to "MEC 301",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Machine Tools"
            )
        )),
        Triple(DEPARTMENT_COMPUTER_ENGINEERING_TECHNOLOGY, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Digital Systems Design",
                COURSE_CODE to "CET 201",
                COURSE_FORMAT to "PDF",
                COURSE_DESCRIPTION to "Digital logic design principles",
                COURSE_PDF_PATH to "path/to/pdf3.pdf"
            )
        )),
        // Continuing with all remaining courses...
        Triple(DEPARTMENT_COMPUTER_ENGINEERING_TECHNOLOGY, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "IoT Fundamentals",
                COURSE_CODE to "CET 401",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Internet of Things applications"
            ),
            mapOf(
                COURSE_TITLE to "Robotics Engineering",
                COURSE_CODE to "CET 402",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "Industrial robotics systems"
            )
        )),
        Triple(DEPARTMENT_ELECTRICAL_ELECTRONICS_ENG_TECH, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Renewable Energy Systems",
                COURSE_CODE to "EEE 421",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Solar and wind energy technologies"
            )
        )),
        Triple(DEPARTMENT_INDUSTRIAL_SAFETY_ENV_TECH, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Waste Management",
                COURSE_CODE to "IET 411",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Hazardous waste handling"
            )
        )),
        Triple(DEPARTMENT_MECHANICAL_ENGINEERING_TECHNOLOGY, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "CAD/CAM",
                COURSE_CODE to "MEC 422",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "Computer-aided manufacturing"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_ENGINEERING_TECHNOLOGY, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Reservoir Engineering",
                COURSE_CODE to "PET 412",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Hydrocarbon reservoir management"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_MARKETING_BUSINESS_STUDIES, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Energy Economics",
                COURSE_CODE to "PMB 412",
                COURSE_FORMAT to "PDF",
                COURSE_DESCRIPTION to "Oil market dynamics",
                COURSE_PDF_PATH to "path/to/pdf7.pdf"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_NATURAL_GAS_PROCESSING_TECH, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Refinery Operations",
                COURSE_CODE to "PNG 412",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Crude oil refining processes"
            )
        )),
        Triple(DEPARTMENT_SCIENCE_LABORATORY_TECHNOLOGY, LEVEL_ND1, listOf(
            mapOf(
                COURSE_TITLE to "Analytical Chemistry",
                COURSE_CODE to "SLT 111",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Chemical analysis methods"
            )
        )),
        Triple(DEPARTMENT_WELDING_ENGINEERING_OFFSHORE_TECH, LEVEL_ND1, listOf(
            mapOf(
                COURSE_TITLE to "Arc Welding Processes",
                COURSE_CODE to "WEO 101",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "SMAW and GTAW techniques"
            )
        )),
        Triple(DEPARTMENT_GENERAL, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Technical Report Writing",
                COURSE_CODE to "GNS 302",
                COURSE_FORMAT to "PDF",
                COURSE_DESCRIPTION to "Engineering documentation",
                COURSE_PDF_PATH to "path/to/pdf8.pdf"
            )
        )),
        Triple(DEPARTMENT_COMPUTER_ENGINEERING_TECHNOLOGY, LEVEL_ND1, listOf(
            mapOf(
                COURSE_TITLE to "Computer Hardware Maintenance",
                COURSE_CODE to "CET 112",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "PC assembly & troubleshooting"
            )
        )),
        Triple(DEPARTMENT_ELECTRICAL_ELECTRONICS_ENG_TECH, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Power Electronics Lab",
                COURSE_CODE to "EEE 312",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "Thyristor applications"
            )
        )),
        Triple(DEPARTMENT_INDUSTRIAL_SAFETY_ENV_TECH, LEVEL_ND2, listOf(
            mapOf(
                COURSE_TITLE to "Construction Safety",
                COURSE_CODE to "IET 222",
                COURSE_FORMAT to "PDF",
                COURSE_DESCRIPTION to "Site safety management",
                COURSE_PDF_PATH to "path/to/construction.pdf"
            )
        )),
        Triple(DEPARTMENT_MECHANICAL_ENGINEERING_TECHNOLOGY, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Heat Transfer",
                COURSE_CODE to "MEC 312",
                COURSE_FORMAT to "PDF",
                COURSE_DESCRIPTION to "Conduction, convection, radiation",
                COURSE_PDF_PATH to "path/to/heat.pdf"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_ENGINEERING_TECHNOLOGY, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Well Completion",
                COURSE_CODE to "PET 312",
                COURSE_FORMAT to "PDF",
                COURSE_DESCRIPTION to "Casing and cementing",
                COURSE_PDF_PATH to "path/to/well.pdf"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_MARKETING_BUSINESS_STUDIES, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Retail Marketing",
                COURSE_CODE to "PMB 422",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "Forecourt management systems"
            )
        )),
        Triple(DEPARTMENT_PETROLEUM_NATURAL_GAS_PROCESSING_TECH, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "LNG Technology",
                COURSE_CODE to "PNG 422",
                COURSE_FORMAT to "PDF",
                COURSE_DESCRIPTION to "Liquefaction processes",
                COURSE_PDF_PATH to "path/to/lng.pdf"
            )
        )),
        Triple(DEPARTMENT_SCIENCE_LABORATORY_TECHNOLOGY, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Environmental Monitoring",
                COURSE_CODE to "SLT 411",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Pollution assessment techniques"
            )
        )),
        Triple(DEPARTMENT_WELDING_ENGINEERING_OFFSHORE_TECH, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Offshore Construction",
                COURSE_CODE to "WEO 401",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Subsea welding operations"
            )
        )),
        Triple(DEPARTMENT_GENERAL, LEVEL_ND1, listOf(
            mapOf(
                COURSE_TITLE to "Nigerian People & Culture",
                COURSE_CODE to "GNS 103",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Cultural heritage studies"
            ),
            mapOf(
                COURSE_TITLE to "French Language I",
                COURSE_CODE to "GNS 113",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Basic communication skills"
            )
        )),
        Triple(DEPARTMENT_GENERAL, LEVEL_HND2, listOf(
            mapOf(
                COURSE_TITLE to "Research Methodology",
                COURSE_CODE to "GNS 411",
                COURSE_FORMAT to "Video",
                COURSE_DESCRIPTION to "Academic research techniques"
            )
        )),
        Triple(DEPARTMENT_GENERAL, LEVEL_HND1, listOf(
            mapOf(
                COURSE_TITLE to "Ethics & Citizenship",
                COURSE_CODE to "GNS 301",
                COURSE_FORMAT to "Text",
                COURSE_DESCRIPTION to "Civic responsibility education"
            )
        ))
    )

    fun populate(scope: CoroutineScope) {
        scope.launch {
            appDatabase.withTransaction {
                try {
                    // Clear existing data first
                    departmentLevelDao.clearAll()
                    courseDao.clearAll()
                    departmentDao.clearAll()
                    levelDao.clearAll()

                    // Insert base entities
                    departmentDao.insertAll(departments)
                    levelDao.insertAll(levels)

                    // Retrieve actual IDs from database
                    val dbDepartments = departmentDao.getAllDepartments()
                    val dbLevels = levelDao.getAllLevels()

                    // Generate all courses
                    val courses = courseDefinitions.flatMap { (deptName, levelName, courseData) ->
                        val department = dbDepartments.find { it.name == deptName }
                            ?: throw IllegalStateException("Department '$deptName' not found in database")
                        val level = dbLevels.find { it.name == levelName }
                            ?: throw IllegalStateException("Level '$levelName' not found in database")

                        courseData.map { data ->
                            createCourseEntity(
                                departmentId = department.id,
                                levelId = level.id,
                                courseTitle = data[COURSE_TITLE] as String,
                                courseCode = data[COURSE_CODE] as String,
                                format = data[COURSE_FORMAT] as String,
                                courseDescription = data[COURSE_DESCRIPTION] as String,
                                pdfPath = data[COURSE_PDF_PATH],
                                imagePath = data[COURSE_IMAGE_PATH],
                                videoPath = data[COURSE_VIDEO_PATH]
                            )
                        }
                    }

                    // Insert courses
                    courseDao.insertAll(courses)

                    // Create department-level relationships
                    val departmentLevelCrossRefs = dbDepartments.flatMap { department ->
                        dbLevels.map { level ->
                            DepartmentLevelCrossRef(department.id, level.id)
                        }
                    }
                    departmentLevelDao.insertAll(departmentLevelCrossRefs)

                    Timber.d("Database population completed successfully")
                } catch (e: Exception) {
                    Timber.e(e, "Database population failed: ${e.message}")
                    throw e // Re-throw the exception to rollback the transaction
                }
            }
        }
    }
}