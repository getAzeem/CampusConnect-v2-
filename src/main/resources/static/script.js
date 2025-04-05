document.addEventListener('DOMContentLoaded', function() {
    // Mobile Navigation Toggle
    const hamburger = document.querySelector('.hamburger');
    const navLinks = document.querySelector('.nav-links');

    hamburger.addEventListener('click', function() {
        this.classList.toggle('active');
        navLinks.classList.toggle('active');
    });

    // Close mobile menu when clicking on a link
    document.querySelectorAll('.nav-links a').forEach(link => {
        link.addEventListener('click', () => {
            hamburger.classList.remove('active');
            navLinks.classList.remove('active');
        });
    });

    // Dark Mode Toggle
    const darkModeToggle = document.getElementById('darkModeToggle');
    const prefersDarkScheme = window.matchMedia('(prefers-color-scheme: dark)');

    // Check for saved user preference or use system preference
    if (localStorage.getItem('darkMode') === 'enabled' ||
        (localStorage.getItem('darkMode') === null && prefersDarkScheme.matches)) {
        document.body.setAttribute('data-theme', 'dark');
        darkModeToggle.checked = true;
    }

    darkModeToggle.addEventListener('change', function() {
        if (this.checked) {
            document.body.setAttribute('data-theme', 'dark');
            localStorage.setItem('darkMode', 'enabled');
        } else {
            document.body.removeAttribute('data-theme');
            localStorage.setItem('darkMode', 'disabled');
        }
    });

    // Testimonial Carousel
    const slides = document.querySelectorAll('.testimonial-slide');
    const indicators = document.querySelectorAll('.indicator');
    const prevBtn = document.querySelector('.carousel-prev');
    const nextBtn = document.querySelector('.carousel-next');
    let currentSlide = 0;

    function showSlide(index) {
        slides.forEach(slide => slide.classList.remove('active'));
        indicators.forEach(indicator => indicator.classList.remove('active'));

        slides[index].classList.add('active');
        indicators[index].classList.add('active');
        currentSlide = index;
    }

    function nextSlide() {
        currentSlide = (currentSlide + 1) % slides.length;
        showSlide(currentSlide);
    }

    function prevSlide() {
        currentSlide = (currentSlide - 1 + slides.length) % slides.length;
        showSlide(currentSlide);
    }

    // Set up event listeners
    nextBtn.addEventListener('click', nextSlide);
    prevBtn.addEventListener('click', prevSlide);

    // Add click events to indicators
    indicators.forEach((indicator, index) => {
        indicator.addEventListener('click', () => showSlide(index));
    });

    // Auto-advance carousel
    let carouselInterval = setInterval(nextSlide, 5000);

    // Pause on hover
    const carousel = document.querySelector('.testimonial-carousel');
    carousel.addEventListener('mouseenter', () => {
        clearInterval(carouselInterval);
    });

    carousel.addEventListener('mouseleave', () => {
        carouselInterval = setInterval(nextSlide, 5000);
    });

    // Smooth scrolling for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();

            const targetId = this.getAttribute('href');
            if (targetId === '#') return;

            const targetElement = document.querySelector(targetId);
            if (targetElement) {
                window.scrollTo({
                    top: targetElement.offsetTop - 80,
                    behavior: 'smooth'
                });
            }
        });
    });

    // Sticky header on scroll
    const header = document.querySelector('.header');
    window.addEventListener('scroll', function() {
        if (window.scrollY > 100) {
            header.style.background = 'rgba(255, 255, 255, 0.95)';
            header.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.1)';
        } else {
            header.style.background = 'var(--white)';
            header.style.boxShadow = 'var(--shadow)';
        }
    });

    // Form submission
    const signupForm = document.querySelector('.signup-form');
    if (signupForm) {
        signupForm.addEventListener('submit', function(e) {
            e.preventDefault();
            const emailInput = this.querySelector('input[type="email"]');
            const email = emailInput.value.trim();

            if (email) {
                // Here you would typically send the data to your server
                alert(`Thank you for signing up! We'll contact you at ${email} shortly.`);
                emailInput.value = '';
            }
        });
    }

    // Club Data Fetching and Display
    async function fetchAndDisplayClubData() {
        try {
            // Show loading state
            document.getElementById('club-name').textContent = 'Loading...';
            document.getElementById('club-description').textContent = 'Fetching club information...';
            const logoImg = document.getElementById('club-logo');
            logoImg.style.opacity = '0';

            // Get club name (default to 'FOSS Club' if no parameter)
            const urlParams = new URLSearchParams(window.location.search);
            const clubName = urlParams.get('club') || 'Github Club';

            // Fetch club details
            const clubResponse = await fetch(`http://localhost:9090/club/getclubbyname?clubName=${encodeURIComponent(clubName)}`);

            if (!clubResponse.ok) {
                throw new Error(`Failed to fetch club data: ${clubResponse.status}`);
            }

            const clubData = await clubResponse.json();

            // Update the hero section with club data
            document.getElementById('club-name').textContent = clubData.cname || clubName;
            document.getElementById('club-description').textContent = clubData.cdiscription ||
                'An all-in-one platform to manage members, events, and communications for your student organization.';

            // Update club logo if available
            if (clubData.clogoUrl) {
                logoImg.src = clubData.clogoUrl;
                logoImg.alt = `${clubData.cname || clubName} Logo`;
                logoImg.onload = () => {
                    logoImg.style.opacity = '1';
                    logoImg.style.transition = 'opacity 0.5s ease';
                };
            } else {
                logoImg.style.opacity = '1'; // Show default image if no logo
            }

            // Optional: Fetch and display members
            await fetchAndDisplayMembers(clubName);

        } catch (error) {
            console.error('Error loading club data:', error);
            document.getElementById('club-name').textContent = 'FOSS Club';
            document.getElementById('club-description').textContent =
                'An error occurred while loading club data. Please try again later.';
            document.getElementById('club-logo').style.opacity = '1';
        }
    }

    // Optional: Function to fetch and display members
    async function fetchAndDisplayMembers(clubName) {
        try {
            const membersResponse = await fetch(
                `http://localhost:9090/club/getclubmembers?clubName=${encodeURIComponent(clubName)}`
            );

            if (membersResponse.ok) {
                const membersData = await membersResponse.json();
                console.log('Club Members:', membersData);
                // Here you could process and display members data
                // For example: create member cards or update a members section
            }
        } catch (error) {
            console.error('Error loading members:', error);
        }
    }

    // Initialize club data fetching
    fetchAndDisplayClubData();

    // Add this to your existing script.js
    async function fetchAndDisplayMembers(clubName = "Github Club") {
        try {
            const response = await fetch(
                `http://localhost:9090/club/getclubmembers?clubName=${encodeURIComponent(clubName)}`
            );

            if (!response.ok) throw new Error('Failed to fetch members');
            const members = await response.json();

            if (members.length === 0) {
                console.log('No members found for this club');
                return;
            }

            // Clear existing carousel items
            document.querySelectorAll('.testimonial-slide').forEach(el => el.remove());
            document.querySelectorAll('.indicator').forEach(el => el.remove());

            const carousel = document.querySelector('.testimonial-carousel');
            const indicatorsContainer = document.querySelector('.carousel-indicators');

            // Create slides for each member
            members.forEach((member, index) => {
                // Create slide
                const slide = document.createElement('div');
                slide.className = `testimonial-slide ${index === 0 ? 'active' : ''}`;
                slide.innerHTML = `
                    <div class="testimonial-content">
                        <div class="member-card">
                            <img src="${member.pfp || 'default-profile.jpg'}"
                                 alt="${member.fullname}"
                                 class="member-photo">
                            <div class="member-info">
                                <h3>${member.fullname}</h3>
                                ${member.github ? `
                                <a href="${member.github}" target="_blank" class="github-link">
                                    <i class="fab fa-github"></i> GitHub Profile
                                </a>` : ''}
                            </div>
                        </div>
                    </div>
                `;
                carousel.insertBefore(slide, document.querySelector('.carousel-controls'));

                // Create indicator
                const indicator = document.createElement('span');
                indicator.className = `indicator ${index === 0 ? 'active' : ''}`;
                indicator.addEventListener('click', () => showSlide(index));
                indicatorsContainer.appendChild(indicator);
            });

            // Reinitialize carousel controls
            setupCarousel();

        } catch (error) {
            console.error('Error loading members:', error);
            // Fallback content
            const carousel = document.querySelector('.testimonial-carousel');
            carousel.innerHTML = `
                <div class="testimonial-slide active">
                    <div class="testimonial-content">
                        <p>Couldn't load member information. Please try again later.</p>
                    </div>
                </div>
                ${carousel.innerHTML}
            `;
        }
    }

    // Update your existing carousel functions
    function setupCarousel() {
        const slides = document.querySelectorAll('.testimonial-slide');
        const indicators = document.querySelectorAll('.indicator');
        const prevBtn = document.querySelector('.carousel-prev');
        const nextBtn = document.querySelector('.carousel-next');
        let currentSlide = 0;

        function showSlide(index) {
            slides.forEach((slide, i) => {
                slide.classList.toggle('active', i === index);
                indicators[i]?.classList.toggle('active', i === index);
            });
            currentSlide = index;
        }

        function nextSlide() {
            currentSlide = (currentSlide + 1) % slides.length;
            showSlide(currentSlide);
        }

        function prevSlide() {
            currentSlide = (currentSlide - 1 + slides.length) % slides.length;
            showSlide(currentSlide);
        }

        // Set up event listeners
        prevBtn?.addEventListener('click', prevSlide);
        nextBtn?.addEventListener('click', nextSlide);

        // Auto-advance
        if (window.carouselInterval) clearInterval(window.carouselInterval);
        window.carouselInterval = setInterval(nextSlide, 5000);

        // Pause on hover
        const carousel = document.querySelector('.testimonial-carousel');
        carousel?.addEventListener('mouseenter', () => clearInterval(window.carouselInterval));
        carousel?.addEventListener('mouseleave', () => {
            window.carouselInterval = setInterval(nextSlide, 5000);
        });
    }

    // Call this after your club data loads
    fetchAndDisplayMembers("DevOps Club");
});