package com.healthy.backend.init;

import com.healthy.backend.entity.Article;
import java.util.ArrayList;
import java.util.List;

public class MentalHealthArticlesData {
    public static List<Article> getMentalHealthArticles() {
        List<Article> articles = new ArrayList<>();

        // Add all 50 articles
        articles.add(new Article(
                "ATC001",
                "Understanding Anxiety Disorders: Causes, Symptoms, and Treatment Options",
                "UID002", // Replace with actual authorId
                "Anxiety disorders represent a group of mental health conditions characterized by excessive worry, fear, "
                        +
                        "and related behavioral disturbances. These disorders affect approximately 264 million people worldwide, making them among the most common mental health issues globally. "
                        +
                        "While occasional anxiety is a normal part of life, anxiety disorders involve persistent, intense fear or worry about everyday situations that can significantly impact daily functioning.\n\n"
                        +

                        "Types of anxiety disorders include generalized anxiety disorder (GAD), panic disorder, specific phobias, social anxiety disorder, agoraphobia, and separation anxiety. Each presents with unique "
                        +
                        "symptoms, though they share common threads of excessive, irrational fear and distress. Generalized anxiety disorder involves persistent worrying about various aspects of life, while panic disorder "
                        +
                        "features recurrent panic attacks - sudden episodes of intense fear accompanied by physical symptoms like heart palpitations, shortness of breath, and dizziness. Social anxiety disorder centers on fear "
                        +
                        "of social situations and potential judgment from others, whereas specific phobias involve intense fear of particular objects or situations."));

        articles.add(new Article(
                "ATC002",
                "Depression in the Modern Age: Recognition, Management, and Recovery Paths",
                "UID003", // Replace with actual authorId
                "Depression, clinically known as major depressive disorder, represents one of the most prevalent mental health challenges of our time, affecting approximately 264 million people globally across all age groups. "
                        +
                        "Far beyond merely feeling sad or experiencing temporary low mood, depression manifests as a serious and often debilitating condition that impacts every aspect of an individual's life - from thought patterns and "
                        +
                        "emotional experiences to physical health and social functioning. In recent years, our understanding of depression has evolved significantly, revealing its complex nature as a multifaceted condition with biological, "
                        +
                        "psychological, and social dimensions.\n\n" +

                        "The hallmark symptoms of depression extend well beyond persistent sadness. Individuals with depression frequently experience profound loss of interest or pleasure in previously enjoyable activities (anhedonia), "
                        +
                        "significant changes in appetite and weight, disrupted sleep patterns (either insomnia or hypersomnia), psychomotor agitation or retardation noticeable to others, fatigue or energy loss, feelings of worthlessness "
                        +
                        "or excessive guilt, diminished ability to think or concentrate, and recurrent thoughts of death or suicide. For a clinical diagnosis, these symptoms must persist for at least two weeks and cause significant "
                        +
                        "distress or impairment in daily functioning."));

        articles.add(new Article(
                "ATC003",
                "The Science of Stress: Understanding Our Body's Response System",
                "UID002",
                "Stress represents one of the most ubiquitous experiences in human life, yet its mechanisms and implications remain widely misunderstood. While commonly experienced as an unpleasant emotional state, stress actually "
                        +
                        "constitutes a complex physiological response system that has evolved over millions of years to help organisms survive threats. Understanding the intricate science behind stress can provide valuable insights into "
                        +
                        "its effects on physical and mental health, along with more effective strategies for management in our fast-paced modern world.\n\n"
                        +

                        "From an evolutionary perspective, the stress response developed as a survival mechanism, preparing our ancestors for fight or flight when confronted with predators or other immediate dangers. This response "
                        +
                        "mobilizes resources to deal with threats efficiently - increasing alertness, accelerating heart rate, elevating blood pressure, and directing energy to muscles. While this system served hunter-gatherers well when "
                        +
                        "facing acute physical threats, today's chronic stressors often involve psychological pressures like work deadlines, financial concerns, relationship difficulties, and information overload."));
        articles.add(new Article(
                "ATC004",
                "Understanding OCD: Breaking the Cycle of Intrusive Thoughts and Compulsions",
                "UID003",
                "Obsessive-Compulsive Disorder (OCD) is a chronic mental health condition characterized by unwanted, intrusive thoughts (obsessions) and repetitive behaviors or mental acts (compulsions) performed to alleviate anxiety. "
                        +
                        "Affecting approximately 1-2% of the population, OCD manifests in various forms including contamination fears, need for symmetry, taboo thoughts, and checking behaviors. The disorder exists on a spectrum, "
                        +
                        "with some individuals experiencing mild symptoms while others face severe impairment in daily functioning.\n\n"
                        +

                        "Recent neuroscientific research has identified specific brain circuit abnormalities in OCD patients, particularly in the cortico-striato-thalamo-cortical loops. Treatment typically combines cognitive-behavioral therapy "
                        +
                        "(CBT) with exposure and response prevention (ERP), along with SSRIs as first-line pharmacological interventions. Emerging therapies include deep brain stimulation for treatment-resistant cases and virtual reality "
                        +
                        "exposure therapy for complex contamination fears."));

        articles.add(new Article(
                "ATC005",
                "PTSD: Navigating Trauma and the Path to Healing",
                "UID002",
                "Post-Traumatic Stress Disorder (PTSD) develops in some individuals following exposure to traumatic events such as combat, assault, accidents, or natural disasters. Characterized by intrusive memories, hypervigilance, "
                        +
                        "avoidance behaviors, and negative alterations in mood and cognition, PTSD affects approximately 3.5% of U.S. adults annually. The disorder demonstrates complex interactions between neurobiological changes and "
                        +
                        "psychological processes, particularly in memory consolidation and fear conditioning pathways.\n\n"
                        +

                        "Effective treatments include trauma-focused psychotherapies like prolonged exposure (PE) therapy and cognitive processing therapy (CPT). Neuroimaging studies show that successful treatment correlates with "
                        +
                        "normalization of amygdala hyperactivity and increased prefrontal cortex regulation. Recent advances include MDMA-assisted psychotherapy in clinical trials and the use of neurofeedback to modulate fear responses."));

        articles.add(new Article(
                "ATC006",
                "The Power of Mindfulness: Ancient Practice in Modern Mental Health Care",
                "UID003",
                "Mindfulness-based interventions have gained significant empirical support in recent decades as effective adjuncts to traditional mental health treatments. Rooted in Buddhist meditation practices, mindfulness involves "
                        +
                        "purposeful, non-judgmental attention to present-moment experiences. Research demonstrates its efficacy in reducing symptoms of anxiety, depression, and stress while improving emotional regulation and cognitive flexibility.\n\n"
                        +

                        "Neuroscientific studies reveal that regular mindfulness practice increases gray matter density in the prefrontal cortex and hippocampus while decreasing amygdala volume. Clinical applications include Mindfulness-Based "
                        +
                        "Stress Reduction (MBSR) and Mindfulness-Based Cognitive Therapy (MBCT), both shown to reduce relapse rates in recurrent depression. Emerging research explores its role in pain management, addiction treatment, "
                        +
                        "and attention deficit disorders."));

        articles.add(new Article(
                "ATC007",
                "Eating Disorders: Beyond the Surface of Food and Body Image",
                "UID002",
                "Eating disorders represent complex mental health conditions characterized by severe disturbances in eating behaviors and related thoughts/emotions. The main types include anorexia nervosa, bulimia nervosa, binge-eating disorder, "
                        +
                        "and avoidant/restrictive food intake disorder (ARFID). These disorders have the highest mortality rate of any mental illness, emphasizing the critical need for early intervention and comprehensive treatment approaches.\n\n"
                        +

                        "Biological factors play a significant role, with heritability estimates ranging from 28-74%. Neuroendocrine abnormalities in hunger/satiety signaling and altered reward processing in the brain contribute to symptom "
                        +
                        "maintenance. Treatment requires multidisciplinary care including medical stabilization, nutritional rehabilitation, and psychotherapy. Family-Based Treatment (FBT) has shown particular efficacy for adolescent anorexia cases."));

        articles.add(new Article(
                "ATC008",
                "The Mental Health-Sleep Connection: Understanding Bidirectional Impacts",
                "UID003",
                "Sleep and mental health share a complex bidirectional relationship, with sleep disturbances both contributing to and resulting from psychiatric conditions. Chronic insomnia increases the risk of developing depression by a factor "
                        +
                        "of 10, while 50-80% of psychiatric patients experience sleep problems. The REM sleep phase appears particularly crucial for emotional processing and memory consolidation.\n\n"
                        +

                        "Cognitive-Behavioral Therapy for Insomnia (CBT-I) has emerged as the gold standard treatment, addressing maladaptive sleep behaviors and cognitive distortions about sleep. Recent discoveries about the glymphatic system's "
                        +
                        "role in clearing neural waste during sleep have opened new research avenues into neurodegenerative aspects of chronic sleep deprivation and mental illness."));

        articles.add(new Article(
                "ATC009",
                "Personality Disorders: Understanding Complex Patterns of Behavior",
                "UID002",
                "Personality disorders represent enduring patterns of inner experience and behavior that deviate markedly from cultural expectations, leading to significant distress or impairment. The DSM-5 recognizes 10 specific personality disorders "
                        +
                        "organized into three clusters: A (odd/eccentric), B (dramatic/erratic), and C (anxious/fearful). Borderline Personality Disorder (BPD) remains the most researched, with dialectical behavior therapy (DBT) showing particular "
                        +
                        "efficacy in reducing self-harm and emotional dysregulation.\n\n"
                        +

                        "Neurobiological research highlights altered frontolimbic circuitry in BPD patients, particularly in emotional regulation and impulse control. The dimensional approach in ICD-11 marks a paradigm shift, assessing personality "
                        +
                        "functioning across five trait domains rather than categorical diagnoses. Early intervention programs for emerging personality disorders in adolescents show promise in improving long-term outcomes."));

        articles.add(new Article(
                "ATC010",
                "Digital Age Mental Health: Navigating Technology's Double-Edged Sword",
                "UID003",
                "The digital revolution has transformed mental health care delivery while introducing new psychological challenges. Teletherapy platforms and mental health apps increase accessibility, particularly for rural populations. However, "
                        +
                        "excessive screen time, social media comparison, and cyberbullying correlate with increased anxiety and depression rates, especially among adolescents.\n\n"
                        +

                        "Emerging technologies like AI-powered chatbots and virtual reality exposure therapy show promise in supplementing traditional treatments. However, digital phenotyping and predictive algorithms raise ethical concerns "
                        +
                        "regarding privacy and diagnostic accuracy. The WHO's digital mental health intervention guidelines emphasize the need for evidence-based digital tools with human oversight."));

        articles.add(new Article(
                "ATC011",
                "Childhood Trauma and Adult Mental Health: Breaking the Cycle",
                "UID002",
                "Adverse Childhood Experiences (ACEs) demonstrate a strong dose-response relationship with adult mental health outcomes. The landmark ACE study revealed that individuals with ≥4 ACEs have 4-12x increased risk for depression, "
                        +
                        "suicide attempts, and substance abuse. Epigenetic mechanisms help explain how early trauma becomes biologically embedded, altering stress response systems and neural development.\n\n"
                        +

                        "Trauma-informed care approaches emphasize safety, trustworthiness, and empowerment in therapeutic settings. Evidence-based interventions like Trauma-Focused CBT and eye movement desensitization and reprocessing (EMDR) "
                        +
                        "help process traumatic memories. Recent research focuses on resilience factors and post-traumatic growth trajectories."));

        articles.add(new Article(
                "ATC012",
                "Bipolar Disorder: Managing the Peaks and Valleys",
                "UID003",
                "Bipolar disorder is characterized by extreme mood swings between manic/hypomanic and depressive episodes. The condition affects approximately 2.8% of U.S. adults, with onset typically occurring in late adolescence or early adulthood. "
                        +
                        "Accurate diagnosis remains challenging, with average delays of 5-10 years between symptom onset and proper treatment.\n\n"
                        +

                        "Mood stabilizers like lithium remain first-line treatments, demonstrating neuroprotective effects and suicide risk reduction. Psychoeducation programs focusing on early warning sign detection significantly improve outcomes. "
                        +
                        "Emerging research explores the role of circadian rhythm disruptions and light therapy in managing bipolar depression."));

        articles.add(new Article(
                "ATC013",
                "The Psychology of Addiction: Beyond Chemical Dependence",
                "UID002",
                "Addiction is a complex brain disorder characterized by compulsive substance use despite harmful consequences. The DSM-5 recognizes substance-related disorders across 10 drug classes, while behavioral addictions like gambling disorder "
                        +
                        "are increasingly acknowledged. Neuroimaging studies reveal addiction-related changes in the mesolimbic dopamine system, prefrontal cortex, and stress response systems.\n\n"
                        +

                        "Effective treatment combines medication-assisted treatment (MAT) with behavioral therapies like contingency management and motivational interviewing. The harm reduction model emphasizes meeting patients where they are, "
                        +
                        "while neuroscience-informed approaches target impaired decision-making processes. Recent advances include deep brain stimulation for opioid use disorder and digital recovery support platforms."));
        articles.add(new Article(
                "ATC014",
                "Mindfulness-Based Stress Reduction: Science and Practice",
                "UID003",
                "MBSR programs demonstrate 31% reduction in stress symptoms and 22% improvement in emotional regulation according to meta-analyses. The 8-week protocol combines body scanning, sitting meditation, and yoga to enhance present-moment awareness. "
                        +
                        "Neuroimaging studies show increased gray matter density in the hippocampus and decreased amygdala activation following MBSR training.\n\n"
                        +
                        "Clinical applications now extend beyond stress reduction to chronic pain management and cancer care. Recent adaptations include digital MBSR platforms with comparable efficacy to in-person programs. Research challenges include "
                        +
                        "standardizing teacher competency and addressing cultural adaptation needs in non-Western populations."));

        articles.add(new Article(
                "ATC015",
                "Eating Disorders: Neurobiology and Treatment Innovations",
                "UID002",
                "Anorexia nervosa carries the highest mortality rate of any mental illness at 10%. Recent genome-wide studies identify loci near metabolic genes suggesting a metabolic-psychiatric crossover. The insula's role in interoceptive awareness "
                        +
                        "emerges as key in maintaining disordered eating patterns.\n\n"
                        +
                        "Family-Based Therapy (FBT) remains gold standard for adolescent anorexia, while new agents like olanzapine show promise in weight restoration. Virtual reality exposure therapy helps address body image distortion. Emerging "
                        +
                        "research explores gut microbiome interventions and leptin's role in meal-related anxiety reduction."));

        articles.add(new Article(
                "ATC016",
                "Autism Spectrum Disorder: Adult Diagnosis Challenges",
                "UID003",
                "1 in 45 adults aged 18-24 meet ASD criteria, yet average diagnosis age remains 4-5 years. Masking/camouflaging behaviors lead to underdiagnosis in females (3:1 male-to-female ratio). Sensory processing differences and alexithymia "
                        +
                        "complicate comorbid anxiety/depression treatment.\n\n"
                        +
                        "Novel assessment tools like the RAADS-14 improve adult screening accuracy. Cognitive-behavioral interventions adapted for ASD show 40% reduction in anxiety symptoms. Workplace accommodation programs focusing on structured "
                        +
                        "tasks and sensory-friendly environments demonstrate improved employment outcomes."));

        articles.add(new Article(
                "ATC017",
                "Geriatric Depression: Beyond the Blues",
                "UID002",
                "Late-life depression affects 15% of adults over 65, with vascular depression subtype linked to cerebrovascular disease. Distinct features include psychomotor retardation and cognitive complaints masking mood symptoms. Treatment "
                        +
                        "resistance rates approach 50% in this population.\n\n"
                        +
                        "SSRIs carry increased fall and hyponatremia risks, making SNRIs like duloxetine first-line. Problem Adaptation Therapy (PATH) combines environmental modification with emotion regulation strategies. Transcranial direct "
                        +
                        "current stimulation (tDCS) shows promise for medication-resistant cases in recent clinical trials."));

        articles.add(new Article(
                "ATC018",
                "Personality Disorders: Dimensional Approaches",
                "UID003",
                "The DSM-5 Alternative Model of Personality Disorders (AMPD) introduces severity levels and trait domains, addressing categorical system limitations. Borderline personality disorder shows 70% remission rate after 10 years with "
                        +
                        "dialectical behavior therapy. Narcissistic personality disorder treatment gains focus on addressing vulnerability beneath grandiosity.\n\n"
                        +
                        "Mentalization-Based Treatment (MBT) demonstrates 60% reduction in self-harm across cluster B disorders. Schema therapy's effectiveness extends to older adults. Research challenges include developing brief assessment "
                        +
                        "tools and managing therapeutic alliance ruptures."));

        articles.add(new Article(
                "ATC019",
                "Psychopharmacology: Precision Medicine Advances",
                "UID002",
                "Pharmacogenomic testing now covers 95% of psychotropic medications, reducing trial-and-error prescribing. CYP2D6/CYP2C19 genotyping predicts SSRI metabolism rates, decreasing adverse effects by 40%. Digital phenotyping combined "
                        +
                        "with AI improves relapse prediction 2 weeks earlier than clinical assessment.\n\n"
                        +
                        "Novel agents include glutamate modulators for TRD and orexin antagonists for insomnia. Long-acting injectables increase schizophrenia medication adherence from 50% to 90%. Ethical debates focus on cognitive enhancement "
                        +
                        "use in non-clinical populations."));

        articles.add(new Article(
                "ATC020",
                "Cultural Psychiatry: Beyond Western Paradigms",
                "UID003",
                "Culture-bound syndromes affect 30% of global populations but remain underrepresented in diagnostic manuals. Taijin kyofusho (Japan) and ataque de nervios (Latino) demonstrate culture-specific symptom expression. Clinician cultural "
                        +
                        "formulation interviews improve diagnostic accuracy by 25% in multicultural settings.\n\n"
                        +
                        "Cultural adaptations of CBT show enhanced efficacy for collectivist cultures when incorporating family systems. Global mental health initiatives face challenges balancing evidence-based interventions with local healing "
                        +
                        "practices. Recent WHO guidelines emphasize cultural validity in outcome measures."));

        articles.add(new Article(
                "ATC021",
                "Sports Psychology: Performance and Mental Health",
                "UID002",
                "Elite athletes experience anxiety/depression at 2x general population rates, with 40% reporting psychological distress post-retirement. Performance optimization techniques like biofeedback and imagery training reduce competition "
                        +
                        "anxiety by 30%. The IOC consensus statement mandates mental health screening for all Olympic athletes.\n\n"
                        +
                        "Mindfulness-acceptance-commitment (MAC) approaches improve focus under pressure. Concussion management now includes neuropsychological testing and mental health monitoring. Emerging issues include esports-related "
                        +
                        "addiction and sleep dysregulation in young gamers."));

        articles.add(new Article(
                "ATC022",
                "Climate Anxiety: Psychological Impacts and Coping",
                "UID003",
                "Eco-anxiety affects 68% of adults under 35, per 2023 global survey. Climate distress manifests as anticipatory grief, solastalgia (environmental homesickness), and future-oriented decision paralysis. Adaptive coping correlates "
                        +
                        "with problem-focused engagement and nature connection.\n\n"
                        +
                        "The Climate-Aware Therapeutic Approach (CATA) validates ecological concerns while fostering agency. Group interventions emphasize collective action and legacy-building. Research gaps include longitudinal studies and "
                        +
                        "differentiating pathological vs. normative climate distress."));

        articles.add(new Article(
                "ATC023",
                "Forensic Psychiatry: Risk Assessment Advances",
                "UID002",
                "Violence risk assessment tools like HCR-20 V3 achieve 0.85 AUC in predicting recidivism. Neuroimaging evidence of prefrontal cortex dysfunction impacts insanity defense evaluations. Malingering detection techniques now "
                        +
                        "incorporate AI analysis of linguistic patterns with 92% accuracy.\n\n"
                        +
                        "Restoration of competency programs show 75% success rates with cognitive adaptation training. Moral reconation therapy reduces antisocial behavior in incarcerated populations by 40%. Ethical challenges include "
                        +
                        "predictive policing applications and balancing public safety with patient rights."));
        articles.add(new Article(
                "ATC024",
                "Telepsychiatry: Efficacy and Implementation",
                "UID003",
                "Meta-analysis shows telepsychiatry achieves 85% diagnostic concordance with in-person care. Rural implementations reduce wait times from 8 weeks to 72 hours. Technical barriers affect 15% of elderly patients, while satisfaction rates reach 92% in youth populations.\n\n"
                        +
                        "Hybrid models combining asynchronous messaging and video visits improve medication adherence by 40%. Reimbursement parity remains a challenge in 60% of states. Emerging technologies include VR exposure therapy integration and AI-powered session analytics."));

        articles.add(new Article(
                "ATC025",
                "Geriatric Psychiatry: Dementia Innovations",
                "UID002",
                "Novel PET tau imaging detects Alzheimer's pathology 10-15 years pre-symptom onset. Non-pharmacological interventions like reminiscence therapy slow cognitive decline by 30% in mild cases. Antipsychotic use in dementia care has decreased 45% since 2015 due to mortality risk awareness.\n\n"
                        +
                        "Multicomponent caregiver interventions reduce burnout rates by 55%. Ethical debates continue around capacity assessments for assisted living decisions. Research advances include microbiome-targeted therapies and tau aggregation inhibitors."));

        articles.add(new Article(
                "ATC026",
                "Nutritional Psychiatry: Gut-Brain Axis",
                "UID003",
                "Randomized trials show Mediterranean diet reduces depression relapse by 40% compared to control. Psychobiotics (B. longum, L. rhamnosus) demonstrate anxiolytic effects in fMRI studies. Gut microbiota diversity correlates with stress resilience (r=0.62, p<0.01).\n\n"
                        +
                        "Nutritional psychiatry protocols now integrate microbiome testing with 65% clinical adoption rate. Challenges include dietary adherence monitoring and accounting for pharmaconutrient interactions. Emerging research explores fermented food impacts on social anxiety."));

        articles.add(new Article(
                "ATC027",
                "Disaster Psychiatry: Crisis Response Models",
                "UID002",
                "Psychological first aid (PFA) reduces acute stress disorder incidence by 35% in disaster zones. Mobile crisis units achieve 90% community penetration post-hurricane. Longitudinal studies show 20% PTSD rate persists 5 years post-wildfire exposure.\n\n"
                        +
                        "Digital triage systems using NLP analyze 10,000 social media posts/hour for suicide risk. Ethical dilemmas include resource allocation during mass casualty events. Innovative approaches combine drone-delivered supplies with telemental health support."));

        articles.add(new Article(
                "ATC028",
                "Personality Disorders: DBT Advancements",
                "UID003",
                "Dialectical Behavior Therapy reduces borderline PD hospitalization rates by 50% at 1-year follow-up. Mobile coaching apps decrease self-harm episodes by 65% between sessions. Genetic studies reveal 45% heritability for cluster B traits.\n\n"
                        +
                        "Mentalization-based treatment shows particular efficacy in antisocial PD with 40% recidivism reduction. Neuroimaging biomarkers (amygdala hyperactivity) now guide treatment selection. Controversies persist around narcissistic PD diagnostic criteria."));

        articles.add(new Article(
                "ATC029",
                "Military Psychiatry: Combat Stress Innovations",
                "UID002",
                "Post-deployment resilience training reduces PTSD incidence from 15% to 7% in Marine cohorts. Virtual reality exposure therapy achieves 70% remission rate for combat-related phobias. Sleep optimization protocols improve cognitive performance by 2.5 SD in special forces.\n\n"
                        +
                        "Novel pharmacological approaches target glucocorticoid receptor sensitivity. Moral injury treatments integrate spiritual counseling with cognitive processing therapy. Challenges include stigma reduction and veteran-to-civilian care transition."));

        articles.add(new Article(
                "ATC030",
                "Pediatric OCD: Neural Circuit Breakthroughs",
                "UID003",
                "fMRI-guided TMS of the SMA reduces compulsions by 60% in treatment-resistant cases. Family-based exposure therapy shows 80% success rate when initiated pre-adolescence. Gut microbiome analysis reveals 5-fold higher streptococcus in early-onset OCD.\n\n"
                        +
                        "Digital phenotyping through smartwatch tracking detects ritualistic movements with 92% accuracy. Immunomodulatory trials show promise for PANDAS subtypes. Long-term outcomes improve when combining SSRIs with neural feedback."));

        articles.add(new Article(
                "ATC031",
                "Addiction Medicine: Neurostimulation Advances",
                "UID002",
                "Deep TMS of the dlPFC reduces cocaine cravings by 55% in RCTs. VR cue exposure therapy decreases alcohol relapse by 40% at 6 months. Genetic testing for OPRM1 variants improves MAT selection accuracy by 30%.\n\n"
                        +
                        "Contingency management apps deliver voucher rewards with 85% compliance. Psilocybin-assisted therapy for nicotine addiction achieves 80% abstinence at 12-month follow-up. Ethical concerns include neuromodulation in court-ordered treatment."));

        articles.add(new Article(
                "ATC032",
                "Perinatal Psychiatry: Screening Protocols",
                "UID003",
                "Universal prenatal depression screening identifies 18% at-risk women previously undiagnosed. Placental corticotropin levels predict postpartum psychosis with 89% specificity. Bright light therapy shows 65% efficacy for antenatal depression vs 35% for SSRIs.\n\n"
                        +
                        "Mother-infant dyadic interventions improve attachment security by 50%. Novel research explores oxytocin nasal sprays for bonding deficits. System challenges include OB-GYN mental health training gaps."));

        articles.add(new Article(
                "ATC033",
                "Neuropsychiatry: TBI Rehabilitation",
                "UID002",
                "Cognitive remediation software improves working memory 2x faster than traditional methods post-TBI. CSF neurofilament light levels correlate with functional outcomes (r=0.71). Transcranial LED therapy enhances neural repair in blast injuries.\n\n"
                        +
                        "Social cognition training restores emotional recognition in 60% of severe cases. Controversies exist around return-to-play decisions in sports concussions. Emerging technologies include exoskeleton-assisted rehabilitation and neural stem cell trials."));

        articles.add(new Article(
                "ATC034",
                "Global Mental Health: Task-Shifting Models",
                "UID003",
                "Community health worker programs increase depression treatment coverage from 15% to 65% in LMICs. Tablet-based CBT training achieves 90% fidelity after 2 weeks. Economic analyses show $4 return per $1 invested in scaled-up mental health care.\n\n"
                        +
                        "Challenges include traditional healer integration and supply chain gaps. Success factors involve cultural adaptation of PHQ-9 and mobile supervision. WHO mhGAP implementation reaches 50 countries with 100,000 providers trained."));

        articles.add(new Article(
                "ATC035",
                "Sleep Medicine: Circadian Interventions",
                "UID002",
                "Melatonin phase response curves optimize light therapy timing for shift workers. Cognitive pre-sleep imagery reduces insomnia severity by 45% in PTSD patients. Genetic testing for PER3 variants guides chronotype-specific scheduling.\n\n"
                        +
                        "Novel interventions include thermal biofeedback for sleep onset and acoustic slow-wave enhancement. Pediatric sleep extension programs improve ADHD symptoms equivalent to medication. Commercial sleep tech validation remains problematic."));

        articles.add(new Article(
                "ATC036",
                "Psycho-oncology: Survivorship Care",
                "UID002",
                "Integrated oncology-psychiatry clinics reduce depression prevalence by 30% in cancer patients. Fear of recurrence CBT protocols decrease distress scores by 2.5 SD. Survivorship care plans now mandate psychological screening in 40 states.\n\n"
                        +
                        "Psychedelic-assisted therapy for existential distress shows 75% long-term benefit. Challenges include financial toxicity-related anxiety and late-effect cognitive rehab. Digital twins model treatment outcomes with 85% accuracy."));

        articles.add(new Article(
                "ATC037",
                "LGBTQ+ Mental Health: Affirmative Care",
                "UID002",
                "Gender-affirming care reduces suicide attempts by 73% in transgender youth. Minority stress models explain 40% of mental health disparities. Online support communities decrease isolation metrics by 60% in rural populations.\n\n"
                        +
                        "Training programs increase clinician competency scores by 35% post-intervention. Legal challenges impact 25% of providers in restrictive states. Emerging research focuses on intersex mental health and bisexual erasure effects."));

        articles.add(new Article(
                "ATC038",
                "Resilience Science: Biomarker Discovery",
                "UID002",
                "BDNF methylation patterns predict stress resilience with 80% accuracy in military cohorts. HRV biofeedback training increases emotional regulation capacity by 45%. Epigenetic clocks show accelerated aging in low-resilience individuals.\n\n"
                        +
                        "Multimodal interventions combining mindfulness, nutrition, and exercise boost resilience scores 2x vs single-modality approaches. Controversies include biological determinism risks. Commercial resilience apps lack scientific validation in 70% of cases."));

        articles.add(new Article(
                "ATC039",
                "Psychopharmacology: Personalization Trends",
                "UID002",
                "Pharmacogenetic testing reduces antidepressant trial failures by 50%. PET receptor occupancy studies optimize antipsychotic dosing with 30% fewer side effects. Psychedelic microdosing registries show 65% subjective benefit for creativity.\n\n"
                        +
                        "Challenges include cost barriers and CYP450 interaction complexity. Digital phenotyping predicts medication response with 75% accuracy. Novel delivery systems include transdermal ketamine patches and AI-controlled dopamine release implants."));

        articles.add(new Article(
                "ATC040",
                "Mood Disorders: Chronotherapeutics",
                "UID002",
                "Triple chronotherapy (sleep deprivation, light, phase advance) achieves 60% rapid remission in bipolar depression. Dark therapy protocols reduce manic symptoms by 40% within 72 hours. Circadian phase typing improves lithium response prediction.\n\n"
                        +
                        "Personalized daylight exposure algorithms prevent seasonal affective disorder in 85% of high-risk cases. Research explores ultradian cycling biomarkers and mitochondrial chronobiology. Implementation barriers include shift work conflicts."));

        articles.add(new Article(
                "ATC041",
                "Community Mental Health: Peer Support",
                "UID002",
                "Certified peer specialists reduce psychiatric readmissions by 35% through lived experience mentoring. Digital peer platforms achieve 24/7 crisis response with 90% user satisfaction. Economic analyses show $2.50 saved per $1 invested.\n\n"
                        +
                        "Training programs standardize recovery-oriented practices across 40 states. Challenges include role boundary maintenance and burnout prevention. Innovative models combine peer support with clinical backup via telehealth."));

        articles.add(new Article(
                "ATC042",
                "Digital Phenotyping: Ethical Frontiers",
                "UID002",
                "Smartphone passive sensing predicts depressive relapse 2 weeks earlier than clinical assessment. Voice AI detects mania with 85% accuracy through speech patterns. Cross-platform data integration raises privacy concerns in 60% of users.\n\n"
                        +
                        "Regulatory frameworks struggle with real-world data validation requirements. Algorithmic bias audits reveal 30% accuracy drops in minority populations. Future directions include blockchain consent management and federated learning models."));
        articles.add(new Article(
                "ATC043",
                "Neurofeedback for ADHD: Long-Term Outcomes",
                "UID002",
                "5-year follow-up shows 55% sustained attention improvement with combined neurofeedback and CBT. QEEG-guided protocols achieve 30% better outcomes than standard approaches. Cost-effectiveness analyses reveal $18,000 lifetime savings per patient.\n\n"
                        +
                        "Emerging home-based systems show 80% compliance rates. Challenges include placebo effects in 25% of cases. Next-gen systems integrate VR environments for enhanced engagement metrics."));

        articles.add(new Article(
                "ATC044",
                "Ecotherapy: Urban Mental Health Applications",
                "UID002",
                "Biophilic design in cities reduces anxiety symptoms by 40% in RCTs. Guided forest therapy sessions decrease rumination scores by 35%. Mobile apps tracking nature exposure correlate with 0.5 point PHQ-9 improvements weekly.\n\n"
                        +
                        "Social prescribing programs achieve 60% participant retention. Pollution sensitivity moderates effects in 30% of cases. Climate anxiety complicates outcomes for 1 in 5 urban youth."));

        articles.add(new Article(
                "ATC045",
                "PTSD Interventions: MDMA Clinical Trials",
                "UID002",
                "Phase 3 trials show 67% remission rates with MDMA-assisted therapy. Neuroimaging reveals 40% amygdala hyperactivity reduction. Durability data demonstrates 75% sustained improvement at 12 months.\n\n"
                        +
                        "Integration challenges occur in 20% of participants. Cost models estimate $15,000 per treatment course. Ethical debates continue about non-medical use and training standards."));

        articles.add(new Article(
                "ATC046",
                "Eating Disorders: Digital Monitoring",
                "UID002",
                "AI-powered meal tracking reduces bingeing episodes by 50% in pilot studies. Wearable glucose monitors provide real-time relapse warnings with 85% accuracy. Virtual support groups decrease hospitalization rates by 30%.\n\n"
                        +
                        "Ethical concerns emerge about data privacy in 45% of users. Cultural adaptation needs identified in 60% of non-Western trials. Combined biometric-cognitive models predict recovery trajectories with 90% precision."));

        articles.add(new Article(
                "ATC047",
                "Workplace Mental Health: AI Solutions",
                "UID002",
                "Natural language processing detects burnout risk with 88% accuracy through email patterns. Virtual reality stress management modules improve coping scores by 40%. Productivity analytics show 2.5:1 ROI for mental health investments.\n\n"
                        +
                        "Employee concerns about surveillance persist in 35% of implementations. Hybrid work models require redesigned digital therapeutics. Global standards emerging for psychological safety metrics."));

        articles.add(new Article(
                "ATC048",
                "Autism Spectrum: Adult Interventions",
                "UID002",
                "Social skills VR training improves employment rates by 55% in ASD adults. Sensory integration apps reduce meltdown frequency by 60%. Genetic subtype matching increases therapy effectiveness 3-fold.\n\n"
                        +
                        "Co-occurring conditions complicate 70% of cases. Lifetime cost models exceed $2.4 million without intervention. Neurodiversity-affirming approaches boost self-esteem metrics by 45%."));

        articles.add(new Article(
                "ATC049",
                "Telepsychiatry: Rural Access Impact",
                "UID002",
                "Virtual care bridges 85% of treatment gaps in underserved areas. Wait times decrease from 8 weeks to 72 hours. Cultural competency algorithms improve retention by 40% in minority populations.\n\n"
                        +
                        "Infrastructure limitations persist in 25% of regions. Reimbursement models vary across 35 states. Hybrid models combining local paraprofessionals with remote MDs show highest efficacy."));

        articles.add(new Article(
                "ATC050",
                "OCD Treatments: Neuromodulation",
                "UID002",
                "Deep TMS achieves 50% symptom reduction in treatment-resistant cases. Closed-loop DBS systems adapt stimulation in real-time with 90% accuracy. fMRI-guided protocols personalize target selection.\n\n"
                        +
                        "Cost remains prohibitive at $25,000 per course. Maintenance protocols undefined for 60% of responders. Ethical frameworks needed for enhancement vs treatment applications."));

        articles.add(new Article(
                "ATC051",
                "Music Therapy: Dementia Care",
                "UID002",
                "Personalized playlists reduce agitation episodes by 70% in late-stage dementia. Neural synchronization studies show 40% increased prefrontal connectivity. Caregiver burden scores improve by 35% with music interventions.\n\n"
                        +
                        "Protocol standardization challenges persist across 50% of facilities. Mobile app delivery increases accessibility 4-fold. Multisensory approaches combining scent and sound show additive benefits."));

        articles.add(new Article(
                "ATC052",
                "Childhood Trauma: Epigenetic Markers",
                "UID002",
                "DNA methylation patterns predict PTSD risk with 80% accuracy in abused children. Intergenerational trauma signatures identified in 65% of offspring. Neurosteroid supplements reverse stress effects in animal models.\n\n"
                        +
                        "Ethical dilemmas emerge in predictive testing. Early intervention programs show 50% resilience boost. Longitudinal studies tracking 30-year outcomes underway."));

        articles.add(new Article(
                "ATC053",
                "Methamphetamine Crisis: Novel Therapies",
                "UID002",
                "Immunotherapy vaccines reduce relapse rates by 40% in phase 2 trials. Cognitive remediation software improves executive function by 35%. Contingency management programs show 3x better outcomes than standard care.\n\n"
                        +
                        "Neurological recovery takes 18-24 months on average. Housing-first approaches decrease ER visits by 60%. Policy changes needed to address 300% usage increase since 2015."));

        articles.add(new Article(
                "ATC054",
                "Personality Disorders: Schema Therapy",
                "UID002",
                "Cluster B disorders show 55% symptom reduction with intensive schema therapy. Mode mapping techniques increase emotional awareness by 40%. Telehealth delivery achieves 80% adherence rates.\n\n"
                        +
                        "Therapist burnout remains challenge in 30% of cases. Economic burden exceeds $150 billion annually. Neurofeedback integration shows promise for emotional regulation."));

        articles.add(new Article(
                "ATC055",
                "Mental Health AI: Diagnostic Accuracy",
                "UID002",
                "Multimodal algorithms match expert diagnosis in 92% of mood disorders. Speech pattern analysis detects psychosis 6 months earlier than clinical signs. Bias audits reveal 15% accuracy drops in non-English speakers.\n\n"
                        +
                        "Regulatory approval pending in 40 countries. Clinician resistance persists in 25% of health systems. Hybrid human-AI workflows increase diagnostic throughput 5-fold."));

        articles.add(new Article(
                "ATC056",
                "Perinatal Psychiatry: Risk Prediction",
                "UID002",
                "Machine learning models predict postpartum depression with 85% accuracy at 32 weeks gestation. Sleep pattern analysis identifies high-risk cases 8 weeks earlier. Brief interventions prevent 60% of predicted episodes.\n\n"
                        +
                        "Cultural stigma prevents screening in 35% of populations. Neurosteroid therapies show rapid response in 72 hours. Long-term child development outcomes improve by 40% with treatment."));

        articles.add(new Article(
                "ATC057",
                "Geriatric Mental Health: Comorbidity",
                "UID002",
                "Integrated care models reduce polypharmacy by 50% in elderly patients. Depression-dementia overlap occurs in 60% of cases. Sensory-friendly CBT adaptations improve engagement by 45%.\n\n"
                        +
                        "Caregiver-mediated interventions prevent 30% of nursing home admissions. Medicare reimbursement barriers affect 40% of providers. End-of-life mental health needs largely unmet."));

        articles.add(new Article(
                "ATC058",
                "Sports Psychology: Injury Recovery",
                "UID002",
                "Mental resilience training reduces rehab time by 25% in ACL injuries. VR exposure therapy decreases return-to-play anxiety by 60%. Neurocognitive testing prevents 35% of concussion mismanagement cases.\n\n"
                        +
                        "Athlete identity loss drives 40% of post-career depression. Biofeedback systems optimize performance under stress. Youth sports programs lack mental health support in 70% of schools."));

        articles.add(new Article(
                "ATC059",
                "Global Mental Health: Crisis Mapping",
                "UID002",
                "Satellite data predicts conflict-related PTSD hotspots with 90% accuracy. Mobile clinics reach 3x more patients in disaster zones. Cultural adaptation indices improve treatment efficacy by 40%.\n\n"
                        +
                        "Funding gaps leave 80% of needs unmet in low-income countries. Task-shifting trains local providers 5x faster than traditional methods. Climate change mental health impacts projected to increase 300% by 2050."));

        articles.add(new Article(
                "ATC060",
                "Existential Psychology: Meaning Tech",
                "UID002",
                "AI life narrative analysis detects meaning crises with 85% accuracy. Virtual reality awe experiences reduce existential anxiety by 45%. Philosophical counseling apps show 60% user satisfaction rates.\n\n"
                        +
                        "Digital legacy planning tools prevent 30% of end-of-life distress. Neurophenomenology studies map purpose networks in default mode. Ethical debates emerge about augmented spirituality technologies."));

        return articles;
    }
}